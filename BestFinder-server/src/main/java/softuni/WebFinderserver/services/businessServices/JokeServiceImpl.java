package softuni.WebFinderserver.services.businessServices;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.UserEntityClone;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.JokeUploadDto;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Game;
import softuni.WebFinderserver.model.entities.categories.Joke;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.JokeCreateView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.JokeRepository;
import softuni.WebFinderserver.services.CommentService;
import softuni.WebFinderserver.services.LikeService;
import softuni.WebFinderserver.services.businessServicesInt.JokeService;
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
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;
    private final CloudUtil cloudUtil;
    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final MessageSource messageSource;

    public JokeServiceImpl(JokeRepository jokeRepository, CloudUtil cloudUtil, UserServiceImpl userService, CommentService commentService, LikeService likeService, MessageSource messageSource) {
        this.jokeRepository = jokeRepository;
        this.cloudUtil = cloudUtil;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
    }

    public BaseView createJoke(JokeUploadDto dto, MultipartFile file) throws IOException {
        Optional<Joke> joke = jokeRepository.findFirstByJokeName(dto.getTorrentName());
        if (joke.isPresent()) {
            throw new UploadTorrentException("Joke with that name already exist",HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }

        Joke mappedGameToBeSaved = mapToJoke(dto, file);
        Joke savedGame = jokeRepository.save(mappedGameToBeSaved);

        return mapToView(savedGame);
    }

    private JokeCreateView mapToView(Joke savedJoke) {
        JokeCreateView build = JokeCreateView.builder()
                .text(savedJoke.getText())
                .torrent("Joke")
                .shortText(savedJoke.getText().substring(0, 14) + "...")
                .jokeName(savedJoke.getJokeName())
                .addedDate(savedJoke.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedJoke.getPictureUrl())
                .countLikes(savedJoke.getLikes().size())
//                .isLikedByUser(isLikedByUser(savedJoke.getLikes()))
                .comments(savedJoke.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .likes(savedJoke.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(), like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .build();
        build.setId(savedJoke.getId());

        return build;
    }

//    private boolean isLikedByUser(List<Like> likes) {
//        if(UserEntityClone.getUserEmail() != null) {
//            likes.removeIf(l -> !l.getUser().getEmail().equals(UserEntityClone.getUserEmail()));
//        }
//        return !likes.isEmpty();
//    }

    private Joke mapToJoke(JokeUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Joke joke = new Joke();
        joke.setJokeName(dto.getTorrentName());
        joke.setLikes(new ArrayList<>());
        joke.setComments(new ArrayList<>());
        joke.setText(dto.getText());
        joke.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        joke.setAddedDate(LocalDate.now());

        return joke;
    }

    public List<BaseView> getAll() {
        List<Joke> jokes = jokeRepository.getAll();
        return jokes.stream().map(this::mapToView).collect(Collectors.toList());
    }
    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        if(criteria.trim().isEmpty() || criteria.isEmpty()) {
            List<Joke> jokes = jokeRepository.getAll();
            List<Joke> list = jokes.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();
            return list.stream().map(this::mapToView).collect(Collectors.toList());
        }
        Set<Joke> allJokes = jokeRepository.getJokeByCriteria(criteria);
        List<Joke> list = allJokes.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();
        return list.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String userEmail) {
        Joke jokes = jokeRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist",HttpStatus.valueOf(400)));
        boolean isLiked = false;
        if (userEmail != null) {
            List<Like> collect = userService.findUserByEmail(userEmail)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == jokes.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(jokes)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new TorrentException("No such joke on add like",HttpStatus.BAD_REQUEST));
        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        joke.getComments().add(new Comment(dto.getComment(), joke, user));
        Joke savedJoke = jokeRepository.save(joke);

        return mapToView(savedJoke);
    }


    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        Joke joke = jokeRepository
                .findById(animeId)
                .orElseThrow(() -> new TorrentException("No such joke when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> collect = joke.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        UserEntity userByEmail = userService.findUserByEmail(userEmail);

        if(userByEmail.getRole().name().equals("ADMIN") || commentService.isOwnerOfComment(commentId, userByEmail.getId())) {
            joke.setComments(collect);
            commentService.deleteCommentById(commentId);
            jokeRepository.saveAndFlush(joke);
            return this.getById(joke.getId(), userEmail);
        }

        throw new UserException("Not allowed to delete another user commend", HttpStatus.valueOf(403));

    }

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {

        Joke joke = jokeRepository.findById(animeId).orElseThrow(() -> new TorrentException("No such joke when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> newCommentList = joke.getComments().stream().map(comment -> {
            if (comment.getId() == commentId) {
                comment.setText(dto.getComment());
                return comment;
            }
            return comment;
        }).toList();
        commentService.editCommentById(commentId, dto.getComment());
        joke.setComments(newCommentList);
        return mapToView(joke);
    }


    public BaseView like(Long id, String userEmail) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new TorrentException("No such joke on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(joke, userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail, savedLike);
        joke.setLikes(likeService.getLikesOfTorrent(id));
        jokeRepository.save(joke);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Joke joke = jokeRepository.findById(id).orElseThrow(() -> new TorrentException("No such joke on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(joke, userByEmail);
        joke.setLikes(likeService.getLikesOfTorrent(id));
        jokeRepository.save(joke);

        return getById(id, userEmail);
    }

    public TorrentInfoView getCategoryInfo(Locale lang) {

        LocalDate movieWhichWasLastAdded =
                jokeRepository.getMovieWhichWasLastAdded();

        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = jokeRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return TorrentInfoView
                .builder()
                .description(setMessageLang(lang, "jokes"))
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