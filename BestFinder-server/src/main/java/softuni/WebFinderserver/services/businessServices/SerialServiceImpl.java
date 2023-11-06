package softuni.WebFinderserver.services.businessServices;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.SerialUploadDto;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Serial;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.SerialsCreateView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.SerialRepository;
import softuni.WebFinderserver.services.*;
import softuni.WebFinderserver.services.businessServicesInt.SerialService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;
import softuni.WebFinderserver.util.CloudUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerialServiceImpl implements SerialService {
    private final SerialRepository serialRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;
    private final ActorCleanerService actorCleanerService;

    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final MessageSource messageSource;

    public SerialServiceImpl(SerialRepository serialRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner, ActorCleanerService actorCleanerService, UserServiceImpl userService, CommentService commentService, LikeService likeService, MessageSource messageSource) {
        this.serialRepository = serialRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.actorCleanerService = actorCleanerService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
    }

    public BaseView createSerial(SerialUploadDto dto, MultipartFile file) throws IOException {
        Optional<Serial> movie = serialRepository.findFirstBySerialName(dto.getTorrentName());

        if (movie.isPresent()) {
            throw new UploadTorrentException("Serial with such name already exist", HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }

        Serial mappedSerialBeforeSaving = mapToSerial(dto, file);
        Serial savedSerial = serialRepository.save(mappedSerialBeforeSaving);

        return mapToView(savedSerial);
    }

    private SerialsCreateView mapToView(Serial savedSerial) {
        int startIndex = savedSerial.getTrailer().length() - 11;
        SerialsCreateView build = SerialsCreateView.builder()
                .serialName(savedSerial.getSerialName())
                .resume(savedSerial.getResume())
                .seasons(savedSerial.getSeasons())
                .addedDate(savedSerial.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedSerial.getPictureUrl())
                .torrent("Serial")
                .categories(savedSerial.getCategories().stream().map(categ -> categ.getCategory().name()).collect(Collectors.joining(", ")))
                .actors(savedSerial.getActors().stream().map(Actor::getFullName).collect(Collectors.joining(", ")))
                .countLikes(savedSerial.getLikes().size())
                .comments(savedSerial.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .videoUrl(savedSerial.getTrailer().substring(startIndex))
                .likes(savedSerial.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(), like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .build();
        build.setId(savedSerial.getId());

        return build;
    }

    private Serial mapToSerial(SerialUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Serial serial = new Serial();
        serial.setSerialName(dto.getTorrentName());
        serial.setLikes(new ArrayList<>());
        serial.setComments(new ArrayList<>());
        serial.setTrailer(dto.getTrailer());
        serial.setResume(dto.getTorrentResume());
        serial.setActors(actorCleanerService
                .getActor(List.of(dto.getActor1(), dto.getActor2(), dto.getActor3(), dto.getActor4(), dto.getActor5())));
        serial.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        serial.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        serial.setAddedDate(LocalDate.now());
        serial.setSeasons(dto.getSeasons());

        return serial;
    }

    public List<BaseView> getAll() {
        List<Serial> serials = serialRepository.getAll();

        return serials.stream().map(this::mapToView).collect(Collectors.toList());
    }


    public List<BaseView> sortBySeasons() {
        return serialRepository.getAllBySeasonsDesc().stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String userEmail) {
        Serial serial = serialRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist", HttpStatus.BAD_REQUEST));
        boolean isLiked = false;
        if (userEmail != null) {
            List<Like> collect = userService
                    .findUserByEmail(userEmail)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == serial.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(serial)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        Serial serial = serialRepository.findById(id).get();
        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        serial.getComments().add(new Comment(dto.getComment(), serial, user));
        Serial savedSerial = serialRepository.save(serial);

        return mapToView(savedSerial);
    }


    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        Serial serial = serialRepository
                .findById(animeId)
                .orElseThrow(() -> new TorrentException("No such serial when deleting comment",HttpStatus.BAD_REQUEST));

        List<Comment> collect = serial.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        serial.setComments(collect);
        commentService.deleteCommentById(commentId);
        serialRepository.saveAndFlush(serial);
        return this.getById(serial.getId(), userEmail);

    }

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {

        Serial serial = serialRepository.findById(animeId).orElseThrow(() -> new TorrentException("No such serial when editing a comment",HttpStatus.BAD_REQUEST));

        List<Comment> newCommentList = serial.getComments().stream().map(comment -> {
            if (comment.getId() == commentId) {
                comment.setText(dto.getComment());
                return comment;
            }
            return comment;
        }).toList();
        commentService.editCommentById(commentId, dto.getComment());
        serial.setComments(newCommentList);
        return mapToView(serial);
    }


    public BaseView like(Long id, String userEmail) {
        Serial serial = serialRepository.findById(id).orElseThrow(() -> new TorrentException("No such serial on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(serial, userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail, savedLike);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Serial serial = serialRepository.findById(id).orElseThrow(() -> new TorrentException("No such serial on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(serial, userByEmail);

        return getById(id, userEmail);
    }

    public TorrentInfoView getCategoryInfo(Locale lang) {
        LocalDate movieWhichWasLastAdded =
                serialRepository.getMovieWhichWasLastAdded();

        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = serialRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return TorrentInfoView
                .builder()
                .description(setMessageLang(lang, "serials"))
                .lastAddedOn(addedOn)
                .build();

    }

    private String setMessageLang(Locale locale, String code) {
        return messageSource.getMessage(
                code,
                new Object[0],
                locale);
    }

}