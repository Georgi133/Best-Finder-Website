package softuni.WebFinderserver.web.anime;

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
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestCategoryProjectionRepository;
import softuni.WebFinderserver.testRepositories.TestAnimeRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class AnimeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestAnimeRepository animeRepository;

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
    void uploadAnimeOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test8@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        GameAnimeUploadDto dto = new GameAnimeUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Anime");
        dto.setTrailer("www.youtbee");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrent("Animes");
        dto.setTorrentName("Spider298");

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        Mockito.when(cloudUtil.upload(file))
                .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-anime")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadAnimeShouldThrowIfThereSuchAnime () throws Exception {

        userRepository.save(testEntityEmailVariable("test238@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        GameAnimeUploadDto dto = new GameAnimeUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Anime");
        dto.setTrailer("www.youtbee");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrentName("TestM8");

        animeRepository.save(testAnimeWithDiffNameAndActor("TestM28", 2004,"comedy"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-anime")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/animes"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFilteredByYearOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Anime Anime1 = testAnimeWithDiffNameAndActor("Tob28" , 2003, "comedy");
        Anime Anime2 = testAnimeWithDiffNameAndActor("Test228", 2004, "action");
        animeRepository.save(Anime1);
        animeRepository.save(Anime2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/animes/filtered-by-year")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        TorrentSearchBarDto dto2 = new TorrentSearchBarDto();
        dto2.setSearchBar("");
        String jsonContent2 = mapToJson(dto2);

        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/animes/filtered-by-year")
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
        Anime Anime1 = testAnimeWithDiffNameAndActor("T8", 2003, "comedy");
        Anime Anime2 = testAnimeWithDiffNameAndActor("Tes268", 2004,"action");
        animeRepository.save(Anime1);
        animeRepository.save(Anime2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/animes/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sortByYearOk() throws Exception {
        Anime Anime1 = testAnimeWithDiffNameAndActor("test228", 2004,"comedy");
        Anime Anime2 = testAnimeWithDiffNameAndActor("Test238", 2003,"action");
        animeRepository.save(Anime1);
        animeRepository.save(Anime2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/animes/sort-by-year"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && contentAsString.contains("COMEDY"));

        int shouldBeFirst = contentAsString.indexOf("2004");
        int shouldBeSecond = contentAsString.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);

    }

    @Test
    void getByIdOk() throws Exception {
        List<Anime> all = animeRepository.findAll();
        Anime Anime = testAnimeWithDiffNameAndActor("Testtt8", 2004,"comedy");
        Long id = animeRepository.save(Anime).getId();
        UserEmailDto dto = new UserEmailDto();
        userRepository.save(testEntityEmailVariable("te8@abv.bg"));
        dto.setUserEmail("te8@abv.bg");
        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/anime/{id}",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("tes8@abv.bg");
        userRepository.save(testEntityEmailVariable("tes8@abv.bg"));

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/anime/{id}",1000L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentOk() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test28@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test28@abv.bg");
        Anime Anime = testAnimeWithDiffNameAndActor("uploadTest", 2004,"comedy");
        Long id = animeRepository.save(Anime).getId();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/anime/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCommentShouldThrowIfAnimeNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/anime/{animeId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfAnimeNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/anime/{animeId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void likeOk() throws Exception {
        Anime Anime = testAnimeWithDiffNameAndActor("LikeMovi8", 1901,"comedy");
        Long id = animeRepository.save(Anime).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeOk8@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeOk8@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/anime/{id}/like", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void likeShouldThrowWhenAnimeDoesNotExist() throws Exception {
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeThrow8@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeThrow8@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/anime/{id}/like", 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Anime Anime = testAnimeWithDiffNameAndActor("LikeAnime8", 1901,"comedy");
        Long id = animeRepository.save(Anime).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("UnLike8@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("UnLike8@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/anime/{id}/unlike", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/anime-info"))
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

    public Anime testAnimeWithDiffNameAndActor(String name, Integer year, String category) {

        CategoryProjection byCategory = categoryProjectionRepository.getByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase()));
        Anime anime = new Anime();
        anime.setAnimeName(name);
        anime.setResume("testAnime");
        anime.setCategories(List.of(byCategory));
        anime.setReleasedYear(year);
        anime.setPictureUrl("pickTest");
        anime.setAddedDate(LocalDate.now());
        anime.setTrailer("youtube.com");
        anime.setCategories(List.of(byCategory));
        anime.setLikes(new ArrayList<>());

        return anime;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


}
