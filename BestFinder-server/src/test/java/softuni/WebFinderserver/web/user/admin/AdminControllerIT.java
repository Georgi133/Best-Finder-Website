package softuni.WebFinderserver.web.user.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import softuni.WebFinderserver.model.dtos.UserChangeRoleDto;
import softuni.WebFinderserver.model.dtos.UserEmailDto;
import softuni.WebFinderserver.model.dtos.UserFindByEmailDto;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.testRepositories.TestBlackListRepository;
import softuni.WebFinderserver.testRepositories.TestUserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private TestBlackListRepository blackListRepository;


    @Autowired
    private AuthenticationManager manager;

    @BeforeEach
    void setUp () {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/admins");
    }

    @AfterEach
    void baseStop () {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void findUserByEmailOk() throws Exception {

        UserEntity entity = new UserEntity();
        entity.setEmail("test2@abv.bg");
        entity.setAge(19);
        entity.setFullName("Test Testov");
        entity.setIpAddress("test");
        entity.setRole(RoleEnum.USER);
        entity.setPass("1234");

        userRepository.save(entity);

        UserFindByEmailDto dto = new UserFindByEmailDto();
        dto.setCurrentUserRole("ADMIN");
        dto.setEmail("test2@abv.bg");


        String jsonRequest = mapToJson(dto);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/find-user")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void changeUserRoleOk() throws Exception {

        UserEntity userEntity = testEntityWithoutArguments();
        userRepository.save(userEntity);

        UserChangeRoleDto dto = new UserChangeRoleDto();
        dto.setEmail("test@abv.bg");
        dto.setRole("ADMIN");
        dto.setChangeUserRole("ADMIN");

        String jsonRequest = mapToJson(dto);
        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/change")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertTrue(userRepository.findByEmail("test@abv.bg").isPresent());
        Assertions.assertEquals("ADMIN",userRepository.findByEmail("test@abv.bg").get().getRole().name());
    }



    @Test
    @WithMockUser(authorities = "ADMIN")
    public void banUserOk() throws Exception {

        UserEntity userEntity = testEntityWithoutArguments();
        userRepository.save(userEntity);

        UserEmailDto dto = new UserEmailDto();
        dto.setUserEmail("test@abv.bg");

        long count = blackListRepository.count();

        String jsonRequest = mapToJson(dto);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/ban-user")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(count + 1 , blackListRepository.count());
    }

    public UserEntity testEntityWithoutArguments() {
        UserEntity entity = new UserEntity();
        entity.setEmail("test@abv.bg");
        entity.setAge(19);
        entity.setFullName("Test Testov");
        entity.setIpAddress("test");
        entity.setRole(RoleEnum.USER);
        entity.setPass("1234");

        return entity;
    }

    public String mapToJson (Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
