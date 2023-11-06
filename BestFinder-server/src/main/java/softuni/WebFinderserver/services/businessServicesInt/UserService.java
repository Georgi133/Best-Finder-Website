package softuni.WebFinderserver.services.businessServicesInt;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.services.exceptions.user.*;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

public interface UserService {

    public void changePassword(UserChangePasswordDto dto);
    public UserRegisterView register(UserRegistrationDto request, HttpServletRequest requestServlet);

    public UserLoginView login(UserLoginDto request);

    public UserInfoView getUserByEmail(String email);


    public UserInfoView editProfile(UserEditProfileDto userEditProfileDto);

    public UserInfoView findByEmail(UserFindByEmailDto dto);

    public UserEntity findUserByEmail(String email) ;

    public UserInfoView changeRole(UserChangeRoleDto dto);

    public void like(UserEntity userByEmail, Like savedLike);

    public void unlike(UserEntity userByEmail);

    public boolean forgottenPassword(UserEmailDto dto, Locale preferredLang) throws MessagingException, UnsupportedEncodingException;

    void banUser(UserEmailDto userEmailDto);

}
