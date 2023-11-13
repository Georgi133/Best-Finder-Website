package softuni.WebFinderserver.web.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.testRepositories.TestUserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private GreenMail greenMail;
    private static TestRestTemplate restTemplate;
    private String baseUrl = "http://localhost";

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private Integer mailPort;
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate();
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @BeforeEach
    void setUp () {
        greenMail = new GreenMail(new ServerSetup(
                mailPort,
                mailHost,
                "smtp"
        ));
        greenMail.start();
        greenMail.setUser(mailUsername,mailPassword);
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/users");
    }

    @AfterEach
    void baseStop () {
        userRepository.deleteAll();
        greenMail.stop();
    }

    @Test
    void registerTest() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test2@abv.bg");
        dto.setPassword("1234");
        dto.setAge(19);
        dto.setFullName("Test Testov");
       restTemplate.postForObject(baseUrl + "/register", dto, UserRegisterView.class);
        Assertions.assertNotNull(userRepository.findAll().stream().map(u -> u.getEmail().equals("test2@abv.bg")));

    }

    @Test
    void registerTestShouldThrowIfUserExist() throws Exception {
        UserEntity userEntity = testEntity("test3@abv.bg");
        userRepository.save(userEntity);

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test3@abv.bg");
        dto.setPassword("1234");
        dto.setAge(19);
        dto.setFullName("Test Testov");
        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }



    @Test
    void loginTest() throws Exception {
        UserEntity userEntity = testEntity("test2@abv.bg");
        userRepository.save(userEntity);

        UserLoginDto dto = new UserLoginDto();
        dto.setEmail("test2@abv.bg");
        dto.setPassword("1234");

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void loginTestShouldThrowIfEmailInvalid() throws Exception {
        UserEntity userEntity = testEntity("test2@abv.bg");
        userRepository.save(userEntity);

        UserLoginDto dto = new UserLoginDto();
        dto.setEmail("test2123@abv.bg");
        dto.setPassword("1234");

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void getUserOk() throws Exception {

        UserEntity entity = testEntity("Pesho1@abv.bg");
        userRepository.save(entity);

        UserChangeProfileDto dto = new UserChangeProfileDto();
        dto.setEmail(testEntity("Pesho1@abv.bg").getEmail());

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-user")
                .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserShouldThrowIfEmailNotExist() throws Exception {

        UserEntity entity = testEntity("NotExist@abv.bg");
        userRepository.save(entity);

        UserChangeProfileDto dto = new UserChangeProfileDto();
        dto.setEmail("alibaba@abv.bg");

        String jsonRequest = mapToJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/get-user")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePasswordIsCreated () throws Exception {

        UserEntity entity = testEntity("Tisho@abv.bg");
        userRepository.save(entity);

        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setEmail(testEntity("Tisho@abv.bg").getEmail());
        dto.setCurrentPassword("1234");
        dto.setNewPassword("12345");
        dto.setConfirmPassword("12345");

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/change-password")
                .content(mapToJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void changePasswordCurrentPasswordDoesNotMatchShouldThrow () throws Exception {

        UserEntity entity = testEntity("Tisho2@abv.bg");
        userRepository.save(entity);

        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setEmail(testEntity("Tisho2@abv.bg").getEmail());
        dto.setCurrentPassword("1235");
        dto.setNewPassword("12345");
        dto.setConfirmPassword("12345");

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/change-password")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordInvalidUserEmailShouldThrow () throws Exception {

        UserEntity entity = testEntity("Tisho3@abv.bg");
        userRepository.save(entity);

        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setEmail(testEntity("Tisho3@abv.bg").getEmail() + 9);
        dto.setCurrentPassword("1234");
        dto.setNewPassword("12345");
        dto.setConfirmPassword("12345");

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/change-password")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordNewPasswordAndConfirmNewPasswordDoesNotMatchShouldThrow () throws Exception {

        UserEntity entity = testEntity("Anton@abv.bg");
        userRepository.save(entity);

        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setEmail(testEntity("Anton@abv.bg").getEmail());
        dto.setCurrentPassword("1234");
        dto.setNewPassword("1234");
        dto.setConfirmPassword("12345");

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/change-password")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editProfileOk () throws Exception {
        UserEntity entity = testEntityWithoutArguments();
        userRepository.save(entity);

        UserEditProfileDto dto = new UserEditProfileDto();
        dto.setEmail("test@abv.bg");
        dto.setFullName("Thincho");
        dto.setAge(33);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit-profile")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertTrue(mvcResult.getResponse()
                .getContentAsString()
                .contains("Thincho"));
    }

    @Test
    void editProfileShouldThrowIfEmailNotExist () throws Exception {
        UserEditProfileDto dto = new UserEditProfileDto();
        dto.setEmail("test@abv.bg");
        dto.setFullName("Thincho");
        dto.setAge(33);

         mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/edit-profile")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void forgottenPasswordOk () throws Exception {
        UserEntity userEntity = testEntityWithoutArguments();
        userRepository.save(userEntity);

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("test@abv.bg");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/forgotten/password")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MimeMessage [] received = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, received.length);
    }

    @Test
    void forgottenPasswordShouldThrowIfUserMailNotValid () throws Exception {
        UserEntity userEntity = testEntityWithoutArguments();
        userRepository.save(userEntity);

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("tes2t@abv.bg");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/forgotten/password")
                        .content(mapToJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }



    public UserEntity testEntity(String email) {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setAge(19);
        entity.setFullName("Test Testov");
        entity.setIpAddress("test");
        entity.setRole(RoleEnum.USER);
        String encodedPass = passwordEncoder.encode("1234");
        entity.setPass(encodedPass);

        return entity;
    }

    public UserEntity testEntityWithoutArguments() {
        UserEntity entity = new UserEntity();
        entity.setEmail("test@abv.bg");
        entity.setAge(19);
        entity.setFullName("Test Testov");
        entity.setIpAddress("test");
        entity.setRole(RoleEnum.USER);
        String encodedPass = passwordEncoder.encode("1234");
        entity.setPass(encodedPass);

        return entity;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }


}
