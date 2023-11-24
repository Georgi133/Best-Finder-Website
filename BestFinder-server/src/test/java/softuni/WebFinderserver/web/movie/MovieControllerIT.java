package softuni.WebFinderserver.web.movie;
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
import softuni.WebFinderserver.model.dtos.MovieUploadDto;
import softuni.WebFinderserver.model.dtos.TorrentSearchBarDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestCategoryProjectionRepository;
import softuni.WebFinderserver.testRepositories.TestMovieRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class MovieControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestMovieRepository movieRepository;

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
    void uploadMovieOk () throws Exception {

        userRepository.save(testEntityEmailVariable("test@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MovieUploadDto dto = new MovieUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Movie");
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

        Mockito.when(cloudUtil.upload(file))
                        .thenReturn("okey");

        Mockito.when(cloudUtil.takeUrl("okey"))
                .thenReturn("okey2");

        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-movie")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void uploadMovieShouldThrowIfThereSuchMovie () throws Exception {

        userRepository.save(testEntityEmailVariable("test23@abv.bg"));

        MockMultipartFile file =
                new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MovieUploadDto dto = new MovieUploadDto();
        dto.setTorrentResume("dadada");
        dto.setReleasedYear(2004);
        dto.setTorrent("Movie");
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

        movieRepository.save(testMovieWithDiffNameAndActor("TestM2", "Tobey1",2004,"comedy"));

        String jsonStr = mapToJson(dto);

        MockMultipartFile jsonFile = new MockMultipartFile("dto", "non", "application/json", jsonStr.getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/upload-movie")
                        .file(file)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get-all/movies"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFilteredByYearOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Movie movie1 = testMovieWithDiffNameAndActor("Tob2", "Jame2" , 2003, "comedy");
        Movie movie2 = testMovieWithDiffNameAndActor("Test22", "James F", 2004, "action");
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        String jsonContent = mapToJson(dto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/movies/filtered-by-year")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && !contentAsString.contains("COMEDY"));

        TorrentSearchBarDto dto2 = new TorrentSearchBarDto();
        dto2.setSearchBar("");
        String jsonContent2 = mapToJson(dto2);

        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/movies/filtered-by-year")
                        .content(jsonContent2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String contentAsString2 = mvcResult2.getResponse().getContentAsString();
        int shouldBeFirst = contentAsString2.indexOf("2004");
        int shouldBeSecond = contentAsString2.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);
        Assertions.assertTrue(contentAsString2.contains("ACTION") && contentAsString2.contains("COMEDY"));
    }

    @Test
    void getAllFilteredByLikesOk() throws Exception {
        TorrentSearchBarDto dto = new TorrentSearchBarDto();
        dto.setSearchBar("act");
        Movie movie1 = testMovieWithDiffNameAndActor("T", "Jamy", 2003, "comedy");
        Movie movie2 = testMovieWithDiffNameAndActor("Tes26", "Jam Fr", 2004,"action");
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        String jsonContent = mapToJson(dto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-all/movies/filtered-by-likes")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && !contentAsString.contains("COMEDY"));
    }

    @Test
    void sortByYearOk() throws Exception {
        Movie movie1 = testMovieWithDiffNameAndActor("test22","To3", 2004,"comedy");
        Movie movie2 = testMovieWithDiffNameAndActor("Test23", "James Fra", 2003,"action");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/movies/sort-by-year"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(contentAsString.contains("ACTION") && contentAsString.contains("COMEDY"));

        int shouldBeFirst = contentAsString.indexOf("2004");
        int shouldBeSecond = contentAsString.indexOf("2003");
        Assertions.assertTrue(shouldBeFirst < shouldBeSecond);

    }

    @Test
    @WithMockUser(username = "te@abv.bg", roles = {"USER"})
    void getByIdOk() throws Exception {
        List<Movie> all = movieRepository.findAll();
        Movie movie = testMovieWithDiffNameAndActor("Testtt","Tobey5", 2004,"comedy");
        Long id = movieRepository.save(movie).getId();
        userRepository.save(testEntityEmailVariable("te@abv.bg"));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/movie/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tes@abv.bg", roles = {"USER"})
    void getByIdShouldThrowIfIdNotValid() throws Exception {
        userRepository.save(testEntityEmailVariable("tes@abv.bg"));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/get/movie/{id}",55L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCommentCreated() throws Exception {

        CommentUploadDto dto = new CommentUploadDto();
        userRepository.save(testEntityEmailVariable("test2@abv.bg"));
        dto.setComment("Here we are");
        dto.setUserEmail("test2@abv.bg");
        Movie movie = testMovieWithDiffNameAndActor("uploadTest", "Uploader", 2004,"comedy");
        Long id = movieRepository.save(movie).getId();

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/upload/movie/{id}/comment",id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "LikeThrow8@abv.bg", roles = {"USER"})
    void deleteCommentShouldThrowIfMovieNotExist() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/movie/{movieId}/comment/{commendId}",1000L, 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editCommentShouldThrowIfMovieNotExist() throws Exception {

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("abv@abv.bg");

        String toJson = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit/movie/{movieId}/comment/{commendId}",1000L, 1000L)
                        .content(toJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "LikeOk@abv.bg", roles = {"USER"})
    void likeOk() throws Exception {
        Movie movie = testMovieWithDiffNameAndActor("LikeMovi", "Liker2", 1901,"comedy");
        Long id = movieRepository.save(movie).getId();
        UserEntity userEntity = testEntityEmailVariable("LikeOk@abv.bg");
        userRepository.save(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/movie/{id}/like", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "LikeThrow@abv.bg", roles = {"USER"})
    void likeShouldThrowWhenMovieDoesNotExist() throws Exception {
        UserEntity userEntity = testEntityEmailVariable("LikeThrow@abv.bg");
        userRepository.save(userEntity);


        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/movie/{id}/like", 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "UnLike@abv.bg", roles = {"USER"})
    void unLikeShouldThrowWhenThereIsNoLikeForDeleting() throws Exception {
        Movie movie = testMovieWithDiffNameAndActor("LikeMovie", "Liker", 1901,"comedy");
        Long id = movieRepository.save(movie).getId();
        UserEntity userEntity = testEntityEmailVariable("UnLike@abv.bg");
        userRepository.save(userEntity);


        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/movie/{id}/unlike", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getInfoOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/movie-info"))
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

    public Movie testMovieWithDiffNameAndActor(String name, String actor, Integer year, String category) {

        CategoryProjection byCategory = categoryProjectionRepository.getByCategory(CategoryProjectionEnum.valueOf(category.toUpperCase()));
        Movie movie = new Movie(name, "dadada", year, List.of(new Actor(actor)),List.of(new CategoryProjection()));
        movie.setPictureUrl("pickTest");
        movie.setAddedDate(LocalDate.now());
        movie.setTrailer("youtube.com");
        movie.setCategories(List.of(byCategory));
        movie.setLikes(new ArrayList<>());

        return movie;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
