package softuni.WebFinderserver.web.serial;

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
import softuni.WebFinderserver.model.dtos.SerialUploadDto;
import softuni.WebFinderserver.model.dtos.TorrentSearchBarDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Serial;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestCategoryProjectionRepository;
import softuni.WebFinderserver.testRepositories.TestSerialRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class SerialControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestSerialRepository serialRepository;

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
    void uploadSerialOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test222@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        SerialUploadDto dto = new SerialUploadDto();
        dto.setTorrentResume("dadada");
        dto.setSeasons(4);
        dto.setTorrent("Serial");
        dto.setTrailer("www.youtbee");
        dto.setActor1("Tobey");
        dto.setActor2("");
        dto.setActor3("");
        dto.setActor4("");
        dto.setActor5("");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrentName("Spider2");

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());

        long count = serialRepository.count();
        Mockito.when(cloudUtil.upload(file))
                .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-serial")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());

        Assertions.assertEquals(count + 1, serialRepository.count());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadSerialShouldThrowIfThereSuchSerial () throws Exception {

        userRepository.save(testEntityEmailVariable("test232@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        SerialUploadDto dto = new SerialUploadDto();
        dto.setTorrentResume("dadada");
        dto.setSeasons(4);
        dto.setTorrent("Serial");
        dto.setTrailer("www.youtbee");
        dto.setActor1("Tobey");
        dto.setActor2("");
        dto.setActor3("");
        dto.setActor4("");
        dto.setActor5("");
        dto.setCategory1("ACTION");
        dto.setCategory2("");
        dto.setCategory3("");
        dto.setTorrentName("TestM2");

        serialRepository.save(testSerialWithDiffNameAndActor("TestM2", "Tobey12",4,"comedy"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-serial")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/serials"))
                .andExpect(status().isOk());
    }


    @Test
    void getAllFilteredByLikesOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Serial serial1 = testSerialWithDiffNameAndActor("T", "Jamy2", 3,"comedy");
        Serial serial2 = testSerialWithDiffNameAndActor("Tes26", "Jam Fr2", 4,"action");
        serialRepository.save(serial1);
        serialRepository.save(serial2);
        String jsonContent = mapToJson(dto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/serials/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && !contentAsString.contains("COMEDY"));
    }


    @Test
    @WithMockUser(username = "te2@abv.bg", roles = {"USER"})
    void getByIdOk() throws Exception {
        Serial Serial = testSerialWithDiffNameAndActor("Testtt","Tobey52", 4, "comedy");
        Long id = serialRepository.save(Serial).getId();
        userRepository.save(testEntityEmailVariable("te2@abv.bg"));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/serial/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tes2@abv.bg", roles = {"USER"})
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        userRepository.save(testEntityEmailVariable("tes2@abv.bg"));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/serial/{id}",55L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentCreated() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test22@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test22@abv.bg");
        Serial serial = testSerialWithDiffNameAndActor("uploadTest", "Uploader2", 4, "comedy");
        Long id = serialRepository.save(serial).getId();

        serial.setComments(new ArrayList<>());

        int size = serial.getComments().size();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/serial/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Serial byId = serialRepository.getSerialByTorrentName("uploadTest");

        Assertions.assertEquals(size + 1, byId.getComments().size());
    }

    @Test
    @WithMockUser(username = "LikeThrow8@abv.bg", roles = {"USER"})
    void deleteCommentShouldThrowIfSerialNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/serial/{serialId}/comment/{commendId}",1000L, 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfSerialNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/serial/{serialId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "LikeOk2@abv.bg", roles = {"USER"})
    void likeOk() throws Exception {
        Serial serial = testSerialWithDiffNameAndActor("LikeMovi23", "Liker223", 1901, "comedy");
        Long id = serialRepository.save(serial).getId();
        UserEntity userEntity = testEntityEmailVariable("LikeOk2@abv.bg");
        userRepository.save(userEntity);

        serial.setLikes(new ArrayList<>());
        int size = serial.getLikes().size();

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/serial/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(size + 1, serialRepository.getSerialByTorrentName("LikeMovi23").getLikes().size());
    }

    @Test
    @WithMockUser(username = "LikeThrow8@abv.bg", roles = {"USER"})
    void likeShouldThrowWhenSerialDoesNotExist() throws Exception {
        UserEntity userEntity = testEntityEmailVariable("Like2Throw@abv.bg");
        userRepository.save(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/serial/{id}/like", 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "UnLike2@abv.bg", roles = {"USER"})
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Serial serial = testSerialWithDiffNameAndActor("LikeSerial", "Liker222", 1901, "comedy");
        Long id = serialRepository.save(serial).getId();
        UserEntity userEntity = testEntityEmailVariable("UnLike2@abv.bg");
        userRepository.save(userEntity);


        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/serial/{id}/unlike", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/serial-info"))
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

    public Serial testSerialWithDiffNameAndActor(String name, String actor, Integer seasons, String category) {

        CategoryProjection byCategory = categoryProjectionRepository.getByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase()));
        Serial serial = new Serial();
        serial.setTorrentName(name);
        serial.setActors(List.of(new Actor(actor)));
        serial.setCategories(List.of(byCategory));
        serial.setSeasons(seasons);
        serial.setPictureUrl("pickTest");
        serial.setAddedDate(LocalDate.now());
        serial.setTrailer("youtube.com");
        serial.setCategories(List.of(byCategory));
        serial.setLikes(new ArrayList<>());

        return serial;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


}
