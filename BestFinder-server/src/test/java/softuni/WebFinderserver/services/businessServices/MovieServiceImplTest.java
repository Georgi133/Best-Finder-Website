package softuni.WebFinderserver.services.businessServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.MovieUploadDto;
import softuni.WebFinderserver.model.entities.*;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.CommentView;
import softuni.WebFinderserver.repositories.CategoryProjectionRepository;
import softuni.WebFinderserver.repositories.MovieRepository;
import softuni.WebFinderserver.repositories.UserRepository;
import softuni.WebFinderserver.services.CommentService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;
import softuni.WebFinderserver.services.exceptions.user.UserException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

public class MovieServiceImplTest {

    private final String EMAIL = "scrimr321@abv.bg";
    private final String MOVIE_NAME = "MovieUnit";

    private final String DOES_NOT_MATTER = "no-matter";

    private final String ACTOR_NAME = "Actor";

    private final String CATEGORY = "action";

    private final Long ID = 1L;

    private Movie movie = null;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CategoryProjectionRepository categoryProjectionRepository;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private MovieServiceImpl toTest;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = getMovie(MOVIE_NAME, ACTOR_NAME, 2004, CATEGORY);
    }

    @Test
    public void createMovieShouldThrowIfFileNull() throws IOException {
        MovieUploadDto dto = new MovieUploadDto();
        dto.setTorrentName("Listopad");
       MockMultipartFile file = null;
       Assertions.assertThrows(UploadTorrentException.class,() -> toTest.createMovie(dto,file));
    }

    @Test
    public void deleteCommentById() throws JsonProcessingException {
        Long movieId = ID;
        Long commendId = ID;

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).deleteCommentById(commendId);

        UserEntity user = getUser();

        Comment comment = getComment(movie, user);
        movie.setComments(List.of(comment));
        Assertions.assertEquals("change yourself", movie.getComments().get(0).getText());

        Mockito.when(movieRepository.findById(movieId))
                        .thenReturn(Optional.of(movie));

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(movieRepository).saveAndFlush(movie);

        Mockito.when(userService.findUserByEmail(EMAIL))
                .thenReturn(user);

        Mockito.when(commentService.isOwnerOfComment(commendId,user.getId()))
                .thenReturn(true);

        BaseView baseView = toTest.deleteCommentById(movieId, commendId, EMAIL);

        String stringOfResult = mapString(baseView);

        Assertions.assertFalse(stringOfResult.contains("change yourself"));
    }

    @Test
    public void deleteCommentByIdShouldBeOkayBecauseOfAdminRole() throws JsonProcessingException {
        Long gameId = ID;
        Long commendId = ID;

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).deleteCommentById(commendId);

        UserEntity user = getUser();
        user.setRole(RoleEnum.ADMIN);

        Comment comment = getComment(movie, user);
        movie.setComments(List.of(comment));
        Assertions.assertEquals("change yourself", movie.getComments().get(0).getText());

        Mockito.when(movieRepository.findById(gameId))
                .thenReturn(Optional.of(movie));

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(movieRepository).saveAndFlush(movie);

        Mockito.when(userService.findUserByEmail(EMAIL))
                .thenReturn(user);

        Mockito.when(commentService.isOwnerOfComment(commendId,user.getId()))
                .thenReturn(false);

        BaseView baseView = toTest.deleteCommentById(gameId, commendId, EMAIL);

        String stringOfResult = mapString(baseView);

        Assertions.assertFalse(stringOfResult.contains("change yourself"));
    }

    @Test
    public void deleteCommentByIdShouldThrowIfUserIsNotOwnerOfCommentAndIsNotAdmin() throws JsonProcessingException {
        Long gameId = ID;
        Long commendId = ID;

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).deleteCommentById(commendId);

        UserEntity user = getUser();

        Comment comment = getComment(movie, user);
        movie.setComments(List.of(comment));
        Assertions.assertEquals("change yourself", movie.getComments().get(0).getText());

        Mockito.when(movieRepository.findById(gameId))
                .thenReturn(Optional.of(movie));

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(movieRepository).saveAndFlush(movie);

        Mockito.when(userService.findUserByEmail(EMAIL))
                .thenReturn(user);

        Mockito.when(commentService.isOwnerOfComment(commendId,user.getId()))
                .thenReturn(false);

        Assertions.assertThrows(UserException.class, () -> toTest.deleteCommentById(gameId, commendId, EMAIL));
    }

    @Test
    public void deleteCommentByIdShouldThrowIfMovieNotExist() {
        Long movieId = ID;
        Mockito.when(movieRepository.findById(movieId))
                .thenThrow(TorrentException.class);

        Assertions.assertThrows(TorrentException.class, () -> toTest.deleteCommentById(movieId, ID, EMAIL));
    }


    @Test
    public void editCommentById() throws JsonProcessingException {
        Long movieId = ID;
        Long commentId = ID;
        CommentEditDto dto = new CommentEditDto();
        dto.setComment("change yourself edit");

        UserEntity user = getUser();
        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).editCommentById(commentId, "change yourself edit");

        Comment comment = getComment(movie, user);
        movie.setComments(List.of(comment));

        Mockito.when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        CommentView commentView = new CommentView();
        commentView.setId(ID);
        commentView.setComment("change yourself edit");
        commentView.setFullName(DOES_NOT_MATTER);
        commentView.setUserEmail(EMAIL);

        Mockito.when(commentService.mapToView(comment))
                .thenReturn(commentView);

        BaseView baseView = toTest.editCommentById(movieId, commentId, dto);

        String stringOfResult = mapString(baseView);

        Assertions.assertTrue(stringOfResult.contains("change yourself edit"));
    }

    @Test
    public void editShouldThrowIfMovieNotExist() throws JsonProcessingException {
        Long movieId = ID;
        Mockito.when(movieRepository.findById(movieId))
                .thenThrow(TorrentException.class);
        CommentEditDto dto = new CommentEditDto();

        Assertions.assertThrows(TorrentException.class, () -> toTest.editCommentById(movieId, ID, dto));
    }

    public Movie getMovie(String name, String actor, Integer year, String category) {

        Mockito.when(categoryProjectionRepository.findFirstByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase())))
                .thenReturn(new CategoryProjection(CategoryProjectionEnum.valueOf(category.toUpperCase())));

        CategoryProjection byCategory = categoryProjectionRepository.findFirstByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase()));
        Movie movie = new Movie(name, DOES_NOT_MATTER, year, List.of(new Actor(actor)),List.of(new CategoryProjection()));
        movie.setPictureUrl("pickTest");
        movie.setId(1L);
        movie.setComments(List.of(new Comment()));
        movie.setAddedDate(LocalDate.now());
        movie.setTrailer("youtube.com");
        movie.setCategories(List.of(byCategory));
        movie.setLikes(new ArrayList<>());

        return movie;
    }

    public UserEntity getUser () {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(EMAIL);
        userEntity.setAge(19);
        userEntity.setLikes(List.of(new Like()));
        userEntity.setFullName(DOES_NOT_MATTER);
        userEntity.setIpAddress(DOES_NOT_MATTER);
        userEntity.setLikes(new ArrayList<>());
        userEntity.setRole(RoleEnum.USER);
        userEntity.setId(1L);

        doReturn(Optional.of(userEntity)).when(mockUserRepository)
                .findByEmail(EMAIL);

        return userEntity;
    }

    public Comment getComment (BaseCatalogue category, UserEntity user) {
        Comment comment = new Comment("change yourself",category, user);
        comment.setId(ID);
        return comment;
    }

    public String mapString (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


}
