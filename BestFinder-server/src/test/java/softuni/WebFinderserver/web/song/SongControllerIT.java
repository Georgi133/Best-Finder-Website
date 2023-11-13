package softuni.WebFinderserver.web.song;

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
import softuni.WebFinderserver.model.dtos.SongUploadDto;
import softuni.WebFinderserver.model.dtos.TorrentSearchBarDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.entities.Singer;
import softuni.WebFinderserver.model.entities.SongCategory;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Song;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;
import softuni.WebFinderserver.testRepositories.TestSongRepository;
import softuni.WebFinderserver.testRepositories.TestSongCategoryRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class SongControllerIT {



    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestSongRepository songRepository;

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private TestSongCategoryRepository categorySong;

    @MockBean
    private CloudUtil cloudUtil;

    @BeforeEach
    void setUp () {
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadSongOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test9@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        SongUploadDto dto = new SongUploadDto();
        dto.setReleasedYear(2004);
        dto.setTorrent("Song");
        dto.setSinger1("Singer");
        dto.setSinger2("");
        dto.setSinger3("");
        dto.setSinger4("");
        dto.setSinger5("");
        dto.setSongVideo("www.youtbee");
        dto.setCategory1("POP");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrent("Songs");
        dto.setTorrentName("Spider27");

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        Mockito.when(cloudUtil.upload(file))
                .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-song")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadSongShouldThrowIfThereSuchSong () throws Exception {

        userRepository.save(testEntityEmailVariable("test237@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        SongUploadDto dto = new SongUploadDto();
        dto.setReleasedYear(2004);
        dto.setTorrent("Song");
        dto.setSinger1("Singer2");
        dto.setSinger2("");
        dto.setSinger3("");
        dto.setSinger4("");
        dto.setSinger5("");
        dto.setSongVideo("www.youtbee");
        dto.setCategory1("POP");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrentName("TestM27");

        songRepository.save(testSongWithDiffNameAndActor("TestM27", 2004,"pop","Singer12"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-song")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/songs"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFilteredByYearOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Song Song1 = testSongWithDiffNameAndActor("Tob27" , 2003, "pop","SingerT9");
        Song Song2 = testSongWithDiffNameAndActor("Test227", 2004, "rock","SingerT10");
        songRepository.save(Song1);
        songRepository.save(Song2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/songs/filtered-by-year")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        TorrentSearchBarDto dto2 = new TorrentSearchBarDto();
        dto2.setSearchBar("");
        String jsonContent2 = mapToJson(dto2);

        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/songs/filtered-by-year")
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
        Song Song1 = testSongWithDiffNameAndActor("Ts", 2003, "pop","SingerT7");
        Song Song2 = testSongWithDiffNameAndActor("Tes26s", 2004,"rock","SingerT8");
        songRepository.save(Song1);
        songRepository.save(Song2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/songs/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sortByYearOk() throws Exception {
        Song Song1 = testSongWithDiffNameAndActor("test227", 2004,"pop","SingerT5");
        Song Song2 = testSongWithDiffNameAndActor("Test237", 2003,"rock","SingerT6");
        songRepository.save(Song1);
        songRepository.save(Song2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/songs/sort-by-year"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        int shouldBeFirst = contentAsString.indexOf("2004");
        int shouldBeSecond = contentAsString.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);

    }

    @Test
    void getByIdOk() throws Exception {
        List<Song> all = songRepository.findAll();
        Song Song = testSongWithDiffNameAndActor("Testtt7", 2004,"pop","SingerT4");
        Long id = songRepository.save(Song).getId();
        UserEmailDto dto = new UserEmailDto();
        userRepository.save(testEntityEmailVariable("te7@abv.bg"));
        dto.setUserEmail("te7@abv.bg");
        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/song/{id}",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("tes7@abv.bg");
        userRepository.save(testEntityEmailVariable("tes7@abv.bg"));

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/song/{id}",1000L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentOk() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test27@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test27@abv.bg");
        Song Song = testSongWithDiffNameAndActor("uploadTest", 2004,"pop","SingerT3");
        Long id = songRepository.save(Song).getId();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/song/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCommentShouldThrowIfSongNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/song/{songId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfSongNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/song/{songId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void likeOk() throws Exception {
        Song Song = testSongWithDiffNameAndActor("LikeMovi7", 1901,"pop","SingerT2");
        Long id = songRepository.save(Song).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeOk7@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeOk7@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/song/{id}/like", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void likeShouldThrowWhenSongDoesNotExist() throws Exception {
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeThrow7@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeThrow7@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/song/{id}/like", 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Song Song = testSongWithDiffNameAndActor("LikeSong7", 1901,"pop", "SingerT1");
        Long id = songRepository.save(Song).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("UnLike7@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("UnLike7@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/song/{id}/unlike", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/song-info"))
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

    public Song testSongWithDiffNameAndActor(String name, Integer year, String category, String singer) {

        SongCategory byCategory = categorySong.getByCategory(SongCategoryEnum.valueOf(category.toUpperCase()));
        Song song = new Song();
        song.setSongName(name);
        song.setSingers(List.of(new Singer(singer)));
        song.setCategories(List.of(byCategory));
        song.setReleasedYear(year);
        song.setPictureUrl("pickTest");
        song.setAddedDate(LocalDate.now());
        song.setSongVideo("youtube.com");
        song.setCategories(List.of(byCategory));
        song.setLikes(new ArrayList<>());

        return song;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    
}
