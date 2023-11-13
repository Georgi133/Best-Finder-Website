package softuni.WebFinderserver.web.joke;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.JokeUploadDto;
import softuni.WebFinderserver.model.dtos.TorrentSearchBarDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Joke;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestCategoryProjectionRepository;
import softuni.WebFinderserver.testRepositories.TestJokeRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class JokeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestJokeRepository JokeRepository;

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
    void uploadJokeOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test876@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        JokeUploadDto dto = new JokeUploadDto();
        dto.setText("testtesttesttestt");
        dto.setTorrent("Joke");
        dto.setTorrent("Jokes");
        dto.setTorrentName("Fox7");

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        Mockito.when(cloudUtil.upload(file))
                .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-joke")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadJokeShouldThrowIfThereSuchJoke () throws Exception {

        userRepository.save(testEntityEmailVariable("test236@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        JokeUploadDto dto = new JokeUploadDto();
        dto.setText("testtesttesttest2");
        dto.setTorrent("Joke");
        dto.setTorrent("Jokes");
        dto.setTorrentName("Fox77");

        JokeRepository.save(testJokeWithDiffNameAndActor("Fox77"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-joke")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/jokes"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFilteredByLikesOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Joke Joke1 = testJokeWithDiffNameAndActor("T6");
        Joke Joke2 = testJokeWithDiffNameAndActor("Tes266");
        JokeRepository.save(Joke1);
        JokeRepository.save(Joke2);
        String jsonContent = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/jokes/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void getByIdOk() throws Exception {
        Joke Joke = testJokeWithDiffNameAndActor("Testtt6");
        Long id = JokeRepository.save(Joke).getId();
        UserEmailDto dto = new UserEmailDto();
        userRepository.save(testEntityEmailVariable("te6@abv.bg"));
        dto.setUserEmail("te6@abv.bg");
        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/joke/{id}",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("tes6@abv.bg");
        userRepository.save(testEntityEmailVariable("tes6@abv.bg"));

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get/joke/{id}",1000L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentOk() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test26@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test26@abv.bg");
        Joke Joke = testJokeWithDiffNameAndActor("uploadTest");
        Long id = JokeRepository.save(Joke).getId();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/joke/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCommentShouldThrowIfJokeNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/joke/{jokeId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfJokeNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/joke/{jokeId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void likeOk() throws Exception {
        Joke Joke = testJokeWithDiffNameAndActor("LikeMovi6");
        Long id = JokeRepository.save(Joke).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeOk6@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeOk6@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/joke/{id}/like", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void likeShouldThrowWhenJokeDoesNotExist() throws Exception {
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("LikeThrow6@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("LikeThrow6@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/joke/{id}/like", 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Joke Joke = testJokeWithDiffNameAndActor("LikeJoke6");
        Long id = JokeRepository.save(Joke).getId();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserEmail("UnLike6@abv.bg");
        UserEntity userEntity = testEntityEmailVariable("UnLike6@abv.bg");
        userRepository.save(userEntity);

        String toJson = mapToJson(emailDto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/joke/{id}/unlike", id)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/joke-info"))
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

    public Joke testJokeWithDiffNameAndActor(String name) {

        Joke joke = new Joke();
        joke.setJokeName(name);
        joke.setText("testJokeeeee213");
        joke.setPictureUrl("pickTest");
        joke.setAddedDate(LocalDate.now());
        joke.setLikes(new ArrayList<>());

        return joke;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
    
}
