package softuni.WebFinderserver.web.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.dtos.TorrentSearchBarDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Game;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestCategoryProjectionRepository;
import softuni.WebFinderserver.testRepositories.TestGameRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestGameRepository gameRepository;

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private TestCategoryProjectionRepository categoryProjectionRepository;

    @MockBean
    private CloudUtil cloudUtil;

    @BeforeEach
    void setUp () {
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadGameOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test877@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        GameAnimeUploadDto dto = new GameAnimeUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Game");
        dto.setTrailer("www.youtbee");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrent("games");
        dto.setTorrentName("Spider29");

        long count = gameRepository.count();

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        Mockito.when(cloudUtil.upload(file))
                .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-game")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());

        Assertions.assertEquals(count + 1, gameRepository.count());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadGameShouldThrowIfThereSuchGame () throws Exception {

        userRepository.save(testEntityEmailVariable("test239@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        GameAnimeUploadDto dto = new GameAnimeUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Game");
        dto.setTrailer("www.youtbee");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrentName("TestM2");

        gameRepository.save(getGame("TestM2", 2004,"comedy"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-game")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/games"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFilteredByYearOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Game Game1 = getGame("Tob29" , 2003, "comedy");
        Game Game2 = getGame("Test229", 2004, "action");
        gameRepository.save(Game1);
        gameRepository.save(Game2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/games/filtered-by-year")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        TorrentSearchBarDto dto2 = new TorrentSearchBarDto();
        dto2.setSearchBar("");
        String jsonContent2 = mapToJson(dto2);

        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/games/filtered-by-year")
                        .content(jsonContent2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String contentAsString2 = mvcResult2.getResponse().getContentAsString();
        int shouldBeFirst = contentAsString2.indexOf("2004");
        int shouldBeSecond = contentAsString2.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);
    }

    @Test
    void getAllFilteredByLikesOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Game Game1 = getGame("T9", 2003, "comedy");
        Game Game2 = getGame("Tes269", 2004,"action");
        gameRepository.save(Game1);
        gameRepository.save(Game2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/games/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sortByYearOk() throws Exception {
        Game Game1 = getGame("test229", 2004,"comedy");
        Game Game2 = getGame("Test239", 2003,"action");
        gameRepository.save(Game1);
        gameRepository.save(Game2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/games/sort-by-year"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && contentAsString.contains("COMEDY"));

        int shouldBeFirst = contentAsString.indexOf("2004");
        int shouldBeSecond = contentAsString.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);

    }

    @Test
    @WithMockUser(username = "te9@abv.bg", roles = {"USER"})
    void getByIdOk() throws Exception {
        List<Game> all = gameRepository.findAll();
        Game Game = getGame("Testtt9", 2004,"comedy");
        Long id = gameRepository.save(Game).getId();
        UserEmailDto dto = new UserEmailDto();
        userRepository.save(testEntityEmailVariable("te9@abv.bg"));
        dto.setUserEmail("te9@abv.bg");
        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/game/{id}",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tes9@abv.bg", roles = {"USER"})
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("tes9@abv.bg");
        userRepository.save(testEntityEmailVariable("tes9@abv.bg"));

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/game/{id}",1000L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentCreated() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test29@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test29@abv.bg");
        Game game = getGame("uploadTest", 2004,"comedy");
        Long id = gameRepository.save(game).getId();

        game.setComments(new ArrayList<>());
        int size = game.getComments().size();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/game/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Game byId = gameRepository.getGameByTorrentName("uploadTest");

        Assertions.assertEquals(size + 1, byId.getComments().size());
    }

    @Test
    @WithMockUser(username = "LikeThrow8@abv.bg", roles = {"USER"})
    void deleteCommentShouldThrowIfGameNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/game/{gameId}/comment/{commendId}",1000L, 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfGameNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/game/{gameId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "LikeOk9@abv.bg", roles = {"USER"})
    void likeOk() throws Exception {
        Game game = getGame("LikeMovi9", 1901,"comedy");
        Long id = gameRepository.save(game).getId();
        UserEntity userEntity = testEntityEmailVariable("LikeOk9@abv.bg");
        userRepository.save(userEntity);

        game.setLikes(new ArrayList<>());
        int size = game.getLikes().size();

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/game/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(size + 1, gameRepository.getGameByTorrentName("LikeMovi9").getLikes().size());
    }

    @Test
    @WithMockUser(username = "LikeThrow9@abv.bg", roles = {"USER"})
    void likeShouldThrowWhenGameDoesNotExist() throws Exception {
        UserEntity userEntity = testEntityEmailVariable("LikeThrow9@abv.bg");
        userRepository.save(userEntity);


        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/game/{id}/like", 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "UnLike9@abv.bg", roles = {"USER"})
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Game Game = getGame("LikeGame9", 1901,"comedy");
        Long id = gameRepository.save(Game).getId();
        UserEntity userEntity = testEntityEmailVariable("UnLike9@abv.bg");
        userRepository.save(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/game/{id}/unlike", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/game-info"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    public UserEntity testEntityEmailVariable(String email) {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setAge(19);
        entity.setFullName("Test Testov");
        entity.setIpAddress("test");
        entity.setRole(RoleEnum.USER);
        entity.setPass("1234");

        return entity;
    }

    public Game getGame(String name, Integer year, String category) {

        CategoryProjection byCategory = categoryProjectionRepository.getByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase()));
        Game game = new Game();
        game.setTorrentName(name);
        game.setTorrentResume("testGame");
        game.setCategories(List.of(byCategory));
        game.setReleasedYear(year);
        game.setPictureUrl("pickTest");
        game.setAddedDate(LocalDate.now());
        game.setTrailer("youtube.com");
        game.setLikes(new ArrayList<>());

        return game;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


}
