package softuni.WebFinderserver.services.businessServices;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.SongUploadDto;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.Singer;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.entities.categories.Song;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.SongCreateView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.SongRepository;
import softuni.WebFinderserver.services.*;
import softuni.WebFinderserver.services.businessServicesInt.SongService;
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
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final CloudUtil cloudUtil;
    private final SongCategoryEmptyCleanerService categoryCleaner;
    private final SingerCleanerService singerCleanerService;
    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final MessageSource messageSource;

    public SongServiceImpl(SongRepository songRepository, CloudUtil cloudUtil, SongCategoryEmptyCleanerService categoryCleaner, SingerCleanerService singerCleanerService, UserServiceImpl userService, CommentService commentService, LikeService likeService, MessageSource messageSource) {
        this.songRepository = songRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.singerCleanerService = singerCleanerService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
    }

    public BaseView createSong(SongUploadDto dto, MultipartFile file) throws IOException {
        Optional<Song> song = songRepository.findFirstBySongName(dto.getTorrentName());

        if (song.isPresent()) {
            throw new UploadTorrentException("Song with such name already exist",HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }

        Song mappedMovieBeforeSaving = mapToSong(dto, file);
        Song savedSong = songRepository.save(mappedMovieBeforeSaving);

        return mapToView(savedSong);
    }

    private SongCreateView mapToView(Song savedSong) {
        int startIndex = savedSong.getSongVideo().length() - 11;
        SongCreateView build = SongCreateView.builder()
                .releasedYear(savedSong.getReleasedYear())
                .addedDate(savedSong.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedSong.getPictureUrl())
                .categories(savedSong.getCategories().stream().map(categ -> categ.getCategory().name()).collect(Collectors.joining(", ")))
                .singers(savedSong.getSingers().stream().map(Singer::getFullName).collect(Collectors.joining(", ")))
                .songName(savedSong.getSongName())
                .torrent("Song")
                .countLikes(savedSong.getLikes().size())
                .comments(savedSong.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .videoUrl(savedSong.getSongVideo().substring(startIndex))
                .likes(savedSong.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(), like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .build();
        build.setId(savedSong.getId());

        return build;
    }

    private Song mapToSong(SongUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Song song = new Song();
        song.setSongName(dto.getTorrentName());
        song.setLikes(new ArrayList<>());
        song.setSongVideo(dto.getSongVideo());
        song.setComments(new ArrayList<>());
        song.setSingers(singerCleanerService
                .getSinger(List.of(dto.getSinger1(), dto.getSinger2(), dto.getSinger3(), dto.getSinger4(), dto.getSinger5())));
        song.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        song.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        song.setAddedDate(LocalDate.now());
        song.setReleasedYear(dto.getReleasedYear());

        return song;
    }

    public List<BaseView> getAll() {
        List<Song> songs = songRepository.getAll();

        return songs.stream().map(this::mapToView)
                .collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        List<Song> allMovies = songRepository.getSongsByCriteria(criteria);

        List<Song> list = allMovies.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();

        return list.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByYear(String criteria) {
        List<Song> allMovies = songRepository.getSongsByCriteriaAndOrderedByYearDesc(criteria);

        return allMovies.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> sortByYear() {
        return songRepository.getAllByYearDesc().stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String userEmail) {
        Song song = songRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist",HttpStatus.BAD_REQUEST));
        boolean isLiked = false;
        if (userEmail != null) {
            List<Like> collect = userService
                    .findUserByEmail(userEmail)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == song.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(song)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        Song song = songRepository.findById(id).get();
        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        song.getComments().add(new Comment(dto.getComment(), song, user));
        Song savedSong = songRepository.save(song);

        return mapToView(savedSong);
    }

    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        Song song = songRepository
                .findById(animeId)
                .orElseThrow(() -> new TorrentException("No such song when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> collect = song.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        UserEntity userByEmail = userService.findUserByEmail(userEmail);

        if(userByEmail.getRole().name().equals("ADMIN") || commentService.isOwnerOfComment(commentId, userByEmail.getId())) {
            song.setComments(collect);
            commentService.deleteCommentById(commentId);
            songRepository.saveAndFlush(song);
            return this.getById(song.getId(), userEmail);
        }

        throw new UserException("Not allowed to delete another user commend", HttpStatus.valueOf(403));

    }

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {

        Song movie = songRepository.findById(animeId).orElseThrow(() -> new TorrentException("No such song when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> newCommentList = movie.getComments().stream().map(comment -> {
            if (comment.getId() == commentId) {
                comment.setText(dto.getComment());
                return comment;
            }
            return comment;
        }).toList();
        commentService.editCommentById(commentId, dto.getComment());
        movie.setComments(newCommentList);
        return mapToView(movie);
    }


    public BaseView like(Long id, String userEmail) {
        Song movie = songRepository.findById(id).orElseThrow(() -> new TorrentException("No such movie on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(movie, userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail, savedLike);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Song movie = songRepository.findById(id).orElseThrow(() -> new TorrentException("No such movie on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(movie, userByEmail);

        return getById(id, userEmail);
    }

    public TorrentInfoView getCategoryInfo(Locale lang) {
        LocalDate movieWhichWasLastAdded =
                songRepository.getMovieWhichWasLastAdded();

        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = songRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return TorrentInfoView
                .builder()
                .description(setMessageLang(lang,"songs"))
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
