package softuni.WebFinderserver.services.businessServices;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Game;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.GameCreateView;
import softuni.WebFinderserver.model.views.LikeView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.repositories.GameRepository;
import softuni.WebFinderserver.services.CategoryEmptyCleanerService;
import softuni.WebFinderserver.services.CommentService;
import softuni.WebFinderserver.services.LikeService;
import softuni.WebFinderserver.services.businessServicesInt.GameService;
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
public class GameServiceImpl implements GameService {

    private final ModelMapper mapper;
    private final GameRepository gameRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;
    private final UserServiceImpl userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final MessageSource messageSource;

    public GameServiceImpl(ModelMapper mapper, GameRepository gameRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner, UserServiceImpl userService, CommentService commentService, LikeService likeService, MessageSource messageSource) {
        this.mapper = mapper;
        this.gameRepository = gameRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.messageSource = messageSource;
    }

    public BaseView createGame(GameAnimeUploadDto dto, MultipartFile file) throws IOException {
        Optional<Game> game = gameRepository.findFirstByTorrentName(dto.getTorrentName());
        if (game.isPresent()) {
            throw new UploadTorrentException("Game with such name already exist",HttpStatus.BAD_REQUEST);
        }

        if(file == null) {
            throw new UploadTorrentException("Must have file attached", HttpStatus.BAD_REQUEST);
        }

        Game mappedGameToBeSaved = mapToGame(dto, file);
        Game savedGame = gameRepository.save(mappedGameToBeSaved);

        return mapToView(savedGame);
    }

    private GameCreateView mapToView(Game savedGame) {
        int startIndex = savedGame.getTrailer().length() - 11;
        GameCreateView build = GameCreateView.builder()
                .resume(savedGame.getTorrentResume())
                .gameName(savedGame.getTorrentName())
                .categories(savedGame.getCategories().stream().map(gameCat -> gameCat.getCategory().name()).collect(Collectors.joining(", ")))
                .releasedYear(savedGame.getReleasedYear())
                .addedDate(savedGame.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedGame.getPictureUrl())
                .torrent("Game")
                .countLikes(savedGame.getLikes().size())
                .comments(savedGame.getComments().stream().map(commentService::mapToView).collect(Collectors.toList()))
                .videoUrl(savedGame.getTrailer().substring(startIndex))
                .likes(savedGame.getLikes()
                        .stream()
                        .map(like -> new LikeView(like.getId(), like.getProject().getId(), like.getUser().getEmail()))
                        .collect(Collectors.toList()))
                .build();
        build.setId(savedGame.getId());

        return build;
    }

    private Game mapToGame(GameAnimeUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Game game = mapper.map(dto,Game.class);

        game.setLikes(new ArrayList<>());
        game.setComments(new ArrayList<>());
        game.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        game.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        game.setAddedDate(LocalDate.now());

        return game;
    }


    public List<BaseView> getAll() {
        List<Game> games = gameRepository.getAll();

        return games.stream().map(this::mapToView)
                .collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        Set<Game> allMovies = gameRepository.getAllByCriteria(criteria);

        List<Game> list = allMovies.stream().sorted((m1, m2) -> Integer.compare(m2.getLikes().size(), m1.getLikes().size())).toList();

        return list.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> getAllByCriteriaSortedByYear(String criteria) {
        Set<Game> allMovies = gameRepository.getAllByCriteriaOrderedByYearDesc(criteria);

        return allMovies.stream().map(this::mapToView).collect(Collectors.toList());
    }

    public List<BaseView> sortByYear() {
        return gameRepository.getAllByYearDesc().stream().map(this::mapToView).collect(Collectors.toList());
    }

    public BaseView getById(Long id, String userEmail) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new TorrentException("Such torrent does not exist",HttpStatus.BAD_REQUEST));
        boolean isLiked = false;
        if (userEmail != null) {
            List<Like> collect = userService
                    .findUserByEmail(userEmail)
                    .getLikes()
                    .stream()
                    .filter(like -> like.getProject()
                            .getId() == game.getId()).toList();
            isLiked = !collect.isEmpty();
        }

        return mapToView(game)
                .setLikedByUser(isLiked);
    }

    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        Game game = gameRepository.findById(id).get();
        UserEntity user = userService.findUserByEmail(dto.getUserEmail());
        game.getComments().add(new Comment(dto.getComment(), game, user));
        Game savedGame = gameRepository.save(game);

        return mapToView(savedGame);
    }


    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        Game game = gameRepository
                .findById(animeId)
                .orElseThrow(() -> new TorrentException("No such game when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> collect = game.getComments().stream().filter(comment -> comment.getId() != commentId)
                .collect(Collectors.toList());

        UserEntity userByEmail = userService.findUserByEmail(userEmail);

        if(userByEmail.getRole().name().equals("ADMIN") || commentService.isOwnerOfComment(commentId, userByEmail.getId())) {
            game.setComments(collect);
            commentService.deleteCommentById(commentId);
            gameRepository.saveAndFlush(game);
            return this.getById(game.getId(), userEmail);
        }

        throw new UserException("Not allowed to delete another user commend", HttpStatus.valueOf(403));

    }

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {

        Game game = gameRepository.findById(animeId).orElseThrow(() -> new TorrentException("No such game when deleting comment",HttpStatus.valueOf(403)));

        List<Comment> newCommentList = game.getComments().stream().map(comment -> {
            if (comment.getId() == commentId) {
                comment.setText(dto.getComment());
                return comment;
            }
            return comment;
        }).toList();
        commentService.editCommentById(commentId, dto.getComment());
        game.setComments(newCommentList);
        return mapToView(game);
    }


    public BaseView like(Long id, String userEmail) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new TorrentException("No such game on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        Like like = new Like(game, userByEmail);
        Like savedLike = likeService.saveLike(like);
        userService.like(userByEmail, savedLike);

        return getById(id, userEmail);
    }

    public BaseView unlike(Long id, String userEmail) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new TorrentException("No such game on add like",HttpStatus.BAD_REQUEST));

        UserEntity userByEmail = userService.findUserByEmail(userEmail);
        userByEmail.getLikes().removeIf(like -> like.getProject().getId() == id);

        userService.unlike(userByEmail);

        likeService.removeLike(game, userByEmail);

        return getById(id, userEmail);
    }

    public TorrentInfoView getCategoryInfo(Locale lang) {
        LocalDate movieWhichWasLastAdded =
                gameRepository.getMovieWhichWasLastAdded();

        String addedOn = "";
        if(movieWhichWasLastAdded == null) {
            addedOn = setMessageLang(lang,"notadded");
        }else {
            addedOn = gameRepository.getMovieWhichWasLastAdded().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return TorrentInfoView
                .builder()
                .description(setMessageLang(lang,"games"))
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
