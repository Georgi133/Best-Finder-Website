package softuni.WebFinderserver.services.businessServices;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.UserEntityClone;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.entities.categories.Song;
import softuni.WebFinderserver.model.views.AnimeCreateView;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.AnimeRepository;
import softuni.WebFinderserver.services.CategoryEmptyCleanerService;
import softuni.WebFinderserver.services.CommentService;
import softuni.WebFinderserver.services.LikeService;
import softuni.WebFinderserver.services.businessServicesInt.AnimeService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;
import softuni.WebFinderserver.services.exceptions.user.UserException;
import softuni.WebFinderserver.util.CloudUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimeServiceImpl implements AnimeService {
    private final AnimeRepository animeRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;
    private final UserServiceImpl userService;
    private final CommentService commentService;

    private final LikeService likeService;

    private final MessageSource messageSource;

    public AnimeServiceImpl(AnimeRepository animeRepository,
                            CloudUtil cloudUtil,
                            CategoryEmptyCleanerService categoryCleaner,
                            UserServiceImpl userService,
                            CommentService commentService,
                            LikeService likeService, MessageSource messageSource) {
        this.animeRepository = animeRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
    }

    public AnimeCreateView createAnime(GameAnimeUploadDto dto, MultipartFile file) throws IOException {
        Optional<Anime> anime = animeRepository.findFirstByAnimeName(dto.getTorrentName());
        if (anime.isPresent()) {
            throw new UploadTorrentException("Anime with such name already exist", HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }
        Anime mappedAnimeToBeSaved = mapToAnime(dto, file);
        Anime savedAnime = animeRepository.save(mappedAnimeToBeSaved);

        return mapToView(savedAnime);
    }

    private AnimeCreateView mapToView(Anime savedAnime) {
        int startIndex = savedAnime.getTrailer().length() - 11;
        AnimeCreateView build = AnimeCreateView.builder()
                .likes(savedAnime.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(), like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .resume(savedAnime.getResume())
                .countLikes(savedAnime.getLikes().size())
                .comments(savedAnime.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .animeName(savedAnime.getAnimeName())
                .categories(savedAnime.getCategories().stream().map(categ -> categ.getCategory().name()).collect(Collectors.joining(", ")))
                .releasedYear(savedAnime.getReleasedYear())
//                .isLikedByUser(isLikedByUser(savedAnime.getLikes()))
                .videoUrl(savedAnime.getTrailer().substring(startIndex))
                .torrent("Anime")
                .addedDate(savedAnime.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedAnime.getPictureUrl())
                .build();
        build.setId(savedAnime.getId());

        return build;
    }

//    private boolean isLikedByUser(List<Like> likes) {
//        if(UserEntityClone.getUserEmail() != null) {
//            likes.removeIf(l -> !l.getUser().getEmail().equals(UserEntityClone.getUserEmail()));
//        }
//        return !likes.isEmpty();
//    }

    private Anime mapToAnime(GameAnimeUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Anime anime1 = new Anime();
        anime1.setAnimeName(dto.getTorrentName());
        anime1.setLikes(new ArrayList<>());
        anime1.setTrailer(dto.getTrailer());
        anime1.setComments(new ArrayList<>());
        anime1.setResume(dto.getTorrentResume());
        anime1.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        anime1.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        anime1.setAddedDate(LocalDate.now());
        anime1.setReleasedYear(dto.getReleasedYear());

        return anime1;
    }


    public List<BaseView> getAll() {
        List<Anime> animes = animeRepository.getAll();

        return animes.stream()
                .map(this::mapToView)
                .collect(Collectors.toList());

    }

    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        Set<Anime> allMovies = animeRepository.getAllByCriteria(criteria);

        List<Anime> list = allMovies.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();

        return list.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByYear(String criteria) {
        Set<Anime> allMovies = animeRepository.getAllByCriteriaOrderedByYearDesc(criteria);

        return allMovies.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String email) {
        Anime anime = animeRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist",HttpStatus.BAD_REQUEST));
        boolean isLiked = false;
        if (email != null) {
            List<Like> collect = userService
                    .findUserByEmail(email)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == anime.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(anime)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByAnimeId(Long id, CommentUploadDto dto) {

        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new TorrentException("Such torrent does not exist", HttpStatus.BAD_REQUEST));

        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        anime.getComments().add(new Comment(dto.getComment(), anime, user));
        Anime savedAnime = animeRepository.save(anime);

        return mapToView(savedAnime);
    }

    public BaseView deleteCommentById(Long animeId, Long commentId, String email) {
        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(() -> new TorrentException("No such anime when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> collect = anime.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        UserEntity userByEmail = userService.findUserByEmail(email);

        if(userByEmail.getRole().name().equals("ADMIN") || commentService.isOwnerOfComment(commentId, userByEmail.getId())) {
            anime.setComments(collect);
            commentService.deleteCommentById(commentId);
            animeRepository.saveAndFlush(anime);
            return this.getById(anime.getId(), email);
        }

        throw new UserException("Not allowed to delete another user commend", HttpStatus.valueOf(403));
    }

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {
        Anime anime = animeRepository.findById(animeId).orElseThrow(() -> new TorrentException("No such anime when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> newCommentList = anime.getComments().stream().map(comment -> {
            if (comment.getId() == commentId) {
                comment.setText(dto.getComment());
                return comment;
            }
            return comment;
        }).toList();
        commentService.editCommentById(commentId, dto.getComment());
        anime.setComments(newCommentList);
        return mapToView(anime);
    }


    public BaseView like(Long id, String userEmail) {
        Anime anime = animeRepository.findById(id).orElseThrow(() -> new TorrentException("No such anime on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(anime, userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail, savedLike);

        anime.setLikes(likeService.getLikesOfTorrent(id));
        animeRepository.save(anime);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Anime anime = animeRepository.findById(id).orElseThrow(() -> new TorrentException("No such anime on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(anime, userByEmail);
        anime.setLikes(likeService.getLikesOfTorrent(id));
        animeRepository.save(anime);

        return getById(id, userEmail);
    }

    public List<BaseView> sortByYear() {
        return animeRepository.getAllByYearDesc().stream().map(this::mapToView)
                .collect(Collectors.toList());

    }


    public TorrentInfoView getCategoryInfo(Locale lang) {
        LocalDate movieWhichWasLastAdded =
                animeRepository.getMovieWhichWasLastAdded();

        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = animeRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return TorrentInfoView
                .builder()
                .description(setMessageLang(lang,"animes"))
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