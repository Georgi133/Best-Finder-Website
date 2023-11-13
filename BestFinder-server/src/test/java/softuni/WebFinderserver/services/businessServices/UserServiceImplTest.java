package softuni.WebFinderserver.services.businessServices;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import softuni.WebFinderserver.model.dtos.UserChangeRoleDto;
import softuni.WebFinderserver.model.dtos.UserFindByEmailDto;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.repositories.UserRepository;
import softuni.WebFinderserver.services.exceptions.user.UnAuthorizedException;
import softuni.WebFinderserver.services.exceptions.user.UserException;

import java.util.ArrayList;
import java.util.Optional;

public class UserServiceImplTest {

    private final String EMAIL = "scrimr321@abv.bg";
    private final String ADMIN = "ADMIN";
    private final String USER = "USER";

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl toTest;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserByEmailTest() {

        UserFindByEmailDto dto = new UserFindByEmailDto();
        dto.setEmail(EMAIL);
        dto.setCurrentUserRole("ADMIN");

        getUser();


        UserInfoView byEmail = toTest.findByEmail(dto);

        Assertions.assertEquals(EMAIL,byEmail.getEmail());
    }

    @Test()
    public void getUserByEmailShouldThrowIfUserIsNotAdmin() {

        UserFindByEmailDto dto = new UserFindByEmailDto();
        dto.setEmail(EMAIL);
        dto.setCurrentUserRole(USER);

        Assertions.assertThrows(UnAuthorizedException.class,() -> toTest.findByEmail(dto));
    }

    @Test()
    public void getUserByEmailShouldThrowIfEmailNotValid() {

        UserFindByEmailDto dto = new UserFindByEmailDto();
        dto.setEmail(EMAIL);
        dto.setCurrentUserRole(ADMIN);

        doThrow(UserException.class).when(mockUserRepository)
                .findByEmail(EMAIL);

        Assertions.assertThrows(UserException.class,() -> toTest.findByEmail(dto));
    }

    @Test
    public void changeRole () {
        UserChangeRoleDto dto = new UserChangeRoleDto();
        dto.setEmail(EMAIL);
        dto.setRole(ADMIN);
        dto.setChangeUserRole(ADMIN);

        UserEntity userEntity = getUser();

        doReturn(userEntity).when(mockUserRepository)
                .save(userEntity);

        UserInfoView userInfoView = toTest.changeRole(dto);

        Assertions.assertEquals(ADMIN, userInfoView.getRole().name());

    }

    @Test
    public void changeRoleShouldThrowIfUserNotExist () {
        UserChangeRoleDto dto = new UserChangeRoleDto();
        dto.setEmail(EMAIL);
        dto.setRole(ADMIN);
        dto.setChangeUserRole(ADMIN);

        doThrow(UserException.class).when(mockUserRepository)
                .findByEmail(EMAIL);

        Assertions.assertThrows(UserException.class, () -> toTest.changeRole(dto));
    }

    @Test
    public void changeRoleShouldThrowIfNotAdminRequestsThatMethod () {
        UserChangeRoleDto dto = new UserChangeRoleDto();
        dto.setEmail(EMAIL);
        dto.setRole(USER);
        dto.setChangeUserRole(ADMIN);

        getUser();

        Assertions.assertThrows(UnAuthorizedException.class, () -> toTest.changeRole(dto));
    }


    @Test
    public void changeRoleShouldThrowIfGivenRoleToChangeIsNotValid () {
        UserChangeRoleDto dto = new UserChangeRoleDto();
        dto.setEmail(EMAIL);
        dto.setRole(ADMIN);
        dto.setChangeUserRole("NotExist");

        getUser();


        Assertions.assertThrows(UserException.class, () -> toTest.changeRole(dto));
    }

    public UserEntity getUser () {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(EMAIL);
        userEntity.setAge(19);
        userEntity.setFullName("adaada");
        userEntity.setIpAddress("unkn");
        userEntity.setLikes(new ArrayList<>());
        userEntity.setRole(RoleEnum.USER);
        userEntity.setId(1L);

        doReturn(Optional.of(userEntity)).when(mockUserRepository)
                .findByEmail(EMAIL);

        return userEntity;
    }


}
