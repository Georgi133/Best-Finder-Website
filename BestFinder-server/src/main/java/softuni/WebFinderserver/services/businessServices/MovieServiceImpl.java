package softuni.WebFinderserver.services.businessServices;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.MovieUploadDto;
import softuni.WebFinderserver.model.entities.*;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.MovieCreateView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.MovieRepository;
import softuni.WebFinderserver.services.*;
import softuni.WebFinderserver.services.businessServicesInt.MovieService;
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
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;
    private final ActorCleanerService actorCleanerService;
    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    public MovieServiceImpl(MovieRepository movieRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner, ActorCleanerService actorCleanerService, UserServiceImpl userService, CommentService commentService, LikeService likeService, MessageSource messageSource, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.actorCleanerService = actorCleanerService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
        this.modelMapper = modelMapper;
    }

    public BaseView createMovie (MovieUploadDto dto, MultipartFile file) throws IOException {
        Optional<Movie> movie = movieRepository.findFirstByTorrentName(dto.getTorrentName());

        if(movie.isPresent()) {
            throw new UploadTorrentException("Movie with such name already exist", HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }

        Movie mappedMovieBeforeSaving = mapToMovie(dto, file);
        Movie savedMovie = movieRepository.save(mappedMovieBeforeSaving);

        return mapToView(savedMovie);
    }

    private MovieCreateView mapToView(Movie savedMovie) {

        int startIndex = savedMovie.getTrailer().length() - 11;
        MovieCreateView build = MovieCreateView.builder()
                .movieName(savedMovie.getTorrentName())
                .resume(savedMovie.getTorrentResume())
                .releasedYear(savedMovie.getReleasedYear())
                .addedDate(savedMovie.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedMovie.getPictureUrl())
                .actors(savedMovie.getActors().stream().map(Actor::getFullName).collect(Collectors.joining(", ")))
                .torrent("Movie")
                .categories(savedMovie.getCategories().stream().map(categ -> categ.getCategory().name()).collect(Collectors.joining(", ")))
                .countLikes(savedMovie.getLikes().size())
                .comments(savedMovie.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .videoUrl(savedMovie.getTrailer().substring(startIndex))
                .likes(savedMovie.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(),like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .build();
        build.setId(savedMovie.getId());

        return build;
    }
    private Movie mapToMovie(MovieUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Movie movie = modelMapper.map(dto,Movie.class);

        movie.setLikes(new ArrayList<>());
        movie.setComments(new ArrayList<>());
        movie.setActors(actorCleanerService
                .getActor(List.of(dto.getActor1(),dto.getActor2(),dto.getActor3(),dto.getActor4(),dto.getActor5())));
        movie.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        movie.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        movie.setAddedDate(LocalDate.now());

        return movie;
    }

    public List<BaseView> getAll() {
        List<Movie> allMovies = movieRepository.getAll();

        return allMovies.stream().map(this::mapToView).collect(Collectors.toList());
    }
    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        Set<Movie> allMovies = movieRepository.getMoviesByCriteriaOrderedByLikesDesc(criteria);

        List<Movie> list = allMovies.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();

        return list.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByYear(String criteria) {
        Set<Movie> allMovies = movieRepository.getMoviesByCriteriaOrderedByYearDesc(criteria);

        return allMovies.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> sortByYear() {
        return movieRepository.getAllByYearDesc().stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String userEmail) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist",HttpStatus.BAD_REQUEST));
        boolean isLiked = false;
        if(userEmail != null) {
            List<Like> collect = userService
                    .findUserByEmail(userEmail)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == movie.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(movie)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        Movie movie = movieRepository.findById(id).get();
        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        movie.getComments().add(new Comment(dto.getComment(),movie,user));
        Movie savedMovie = movieRepository.save(movie);

        return mapToView(savedMovie);
    }


    public BaseView deleteCommentById(Long movieId, Long commentId, String userEmail) {
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new TorrentException("No such movie when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> collect = movie.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        UserEntity userByEmail = userService.findUserByEmail(userEmail);

        if(userByEmail.getRole().name().equals("ADMIN") || commentService.isOwnerOfComment(commentId, userByEmail.getId())) {
            movie.setComments(collect);
            commentService.deleteCommentById(commentId);
            movieRepository.saveAndFlush(movie);
            return this.getById(movie.getId(), userEmail);
        }

        throw new UserException("Not allowed to delete another user commend", HttpStatus.valueOf(403));
    }

    public BaseView editCommentById(Long movieId, Long commentId, CommentEditDto dto) {

        Movie movie = movieRepository.
                findById(movieId).orElseThrow(() -> new TorrentException("No such movie when editing a comment",HttpStatus.valueOf(403)));

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
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new TorrentException("No such movie on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(movie,userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail,savedLike);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new TorrentException("No such movie on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(movie, userByEmail);

        return getById(id, userEmail);
    }

    public TorrentInfoView getCategoryInfo(Locale lang) {

        LocalDate movieWhichWasLastAdded =
                movieRepository.getMovieWhichWasLastAdded();
        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = movieRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return TorrentInfoView
                .builder()
              .description(setMessageLang(lang,"movies"))
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
