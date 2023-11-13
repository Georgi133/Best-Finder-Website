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
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.dtos.SongUploadDto;
import softuni.WebFinderserver.model.entities.*;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;
import softuni.WebFinderserver.model.entities.categories.Song;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.CommentView;
import softuni.WebFinderserver.repositories.SongCategoryRepository;
import softuni.WebFinderserver.repositories.SongRepository;
import softuni.WebFinderserver.repositories.UserRepository;
import softuni.WebFinderserver.services.CommentService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

public class SongServiceImplTest {

    private final String EMAIL = "scrimr321@abv.bg";
    private final String SONG_NAME = "SongUnit";

    private final String DOES_NOT_MATTER = "no-matter";

    private final Long ID = 1L;
    @Mock
    private SongRepository songRepository;
    @Mock
    private SongCategoryRepository songCategory;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private SongServiceImpl toTest;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createMovieShouldThrowIfFileNull() throws IOException {
        SongUploadDto dto = new SongUploadDto();
        dto.setTorrentName("Listopad");
        MockMultipartFile file = null;
        Assertions.assertThrows(UploadTorrentException.class,() -> toTest.createSong(dto,file));
    }

    @Test
    public void deleteCommentById() throws JsonProcessingException {
        Long gameId = ID;
        Long commendId = ID;

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).deleteCommentById(commendId);

        Song song = getGame(SONG_NAME, 2004, "pop");

        UserEntity user = getUser();

        Comment comment = getComment(song, user);
        song.setComments(List.of(comment));
        Assertions.assertEquals("change yourself", song.getComments().get(0).getText());

        Mockito.when(songRepository.findById(gameId))
                .thenReturn(Optional.of(song));

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(songRepository).saveAndFlush(song);

        Mockito.when(userService.findUserByEmail(EMAIL))
                .thenReturn(user);

        BaseView baseView = toTest.deleteCommentById(gameId, commendId, EMAIL);

        String stringOfResult = mapString(baseView);

        Assertions.assertFalse(stringOfResult.contains("change yourself"));
    }

    @Test
    public void deleteCommentByIdShouldThrowIfMovieNotExist() {
        Long movieId = ID;
        Mockito.when(songRepository.findById(movieId))
                .thenThrow(TorrentException.class);

        Assertions.assertThrows(TorrentException.class, () -> toTest.deleteCommentById(movieId, ID, EMAIL));
    }


    @Test
    public void editCommentById() throws JsonProcessingException {
        Long movieId = ID;
        Long commentId = ID;
        CommentEditDto dto = new CommentEditDto();
        dto.setComment("change yourself edit");
        Song song = getGame(SONG_NAME, 2004, "pop");

        UserEntity user = getUser();
        Mockito.doAnswer(invocation -> {
            return null;
        }).when(commentService).editCommentById(commentId, "change yourself edit");

        Comment comment = getComment(song, user);
        song.setComments(List.of(comment));

        Mockito.when(songRepository.findById(movieId))
                .thenReturn(Optional.of(song));

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
        Mockito.when(songRepository.findById(movieId))
                .thenThrow(TorrentException.class);
        CommentEditDto dto = new CommentEditDto();

        Assertions.assertThrows(TorrentException.class, () -> toTest.editCommentById(movieId, ID, dto));
    }

    public Song getGame(String name, Integer year, String category) {

        Mockito.when(songCategory.findFirstByCategory(SongCategoryEnum.valueOf(category.toUpperCase())))
                .thenReturn(new SongCategory(SongCategoryEnum.valueOf(category.toUpperCase())));

        SongCategory byCategory = songCategory.findFirstByCategory(SongCategoryEnum.valueOf(category.toUpperCase()));
        Song song = new Song();
        song.setId(ID);
        song.setSingers(List.of(new Singer()));
        song.setSongName(name);
        song.setReleasedYear(year);
        song.setPictureUrl("pickTest");
        song.setAddedDate(LocalDate.now());
        song.setSongVideo("youtube.com");
        song.setCategories(List.of(byCategory));
        song.setLikes(new ArrayList<>());

        return song;
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
