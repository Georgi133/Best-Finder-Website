package softuni.WebFinderserver.services.businessServices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.services.jwt.JwtService;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.entities.ForgottenPasswordEmailMessageEvent;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.repositories.UserRepository;
import softuni.WebFinderserver.services.businessServicesInt.BlackListService;
import softuni.WebFinderserver.services.businessServicesInt.UserService;
import softuni.WebFinderserver.services.exceptions.user.*;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BlackListService blackListService;
    private final ApplicationEventPublisher publisher;

    public UserRegisterView register(UserRegistrationDto request, HttpServletRequest requestServlet) {
        if (isUserExist(request.getEmail())) {
            throw new InvalidRegisterException("There is user with such email", HttpStatus.valueOf(403));
        }

        String ipAddress = getIpAddress(requestServlet);

        var user = UserEntity.builder()

                .email(request.getEmail())
                .pass(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.USER)
                .fullName(request.getFullName())
                .age(request.getAge())
                .ipAddress(ipAddress)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return UserRegisterView.builder()
                .token(jwtToken)
                .build();
    }

    private boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserLoginView login(UserLoginDto request) {

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (RuntimeException e) {
            throw new InvalidLoginException("Wrong email or password", HttpStatus.valueOf(401));
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidLoginException("Wrong email or password", HttpStatus.valueOf(401)));

        var jwtToken = jwtService.generateToken(user);

        return UserLoginView.builder()
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .age(user.getAge())
                .role(user.getRole())
                .build();
    }

    public String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.equals("unknown")) {
            ipAddress = xForwardedForHeader;
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public void changePassword(UserChangePasswordDto dto) {

        UserEntity userEntity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException("Email of user is not valid when changing password", HttpStatus.valueOf(401)));

        if (dto.getNewPassword().equals(dto.getConfirmPassword())) {
            if (arePasswordsMatching(dto.getCurrentPassword(), userEntity.getPassword())) {
                userEntity.setPass(passwordEncoder.encode(dto.getNewPassword()));
            } else {
                throw new InvalidPasswordException("Current password does not match", HttpStatus.valueOf(401));
            }
        } else {
            throw new InvalidPasswordException("New passwords don't match", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(userEntity);
    }

    public boolean arePasswordsMatching(String rawPassword, String userPassword) {
        return passwordEncoder.matches(rawPassword, userPassword);
    }

    public UserInfoView getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Email of user not valid when fetching data for profile change", HttpStatus.BAD_REQUEST));

        return UserInfoView
                .builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .age(userEntity.getAge())
                .role(userEntity.getRole())
                .build();
    }

    public UserInfoView editProfile(UserEditProfileDto userEditProfileDto) {
        UserEntity userEntity = userRepository.findByEmail(userEditProfileDto.getEmail())
                .orElseThrow(() -> new UserException("User with such email doesn't exist on editing profile", HttpStatus.valueOf(401)));

        userEntity.setAge(userEditProfileDto.getAge());
        userEntity.setFullName(userEditProfileDto.getFullName());

        UserEntity save = userRepository.save(userEntity);

        return UserInfoView.builder()
                .id(save.id)
                .role(save.getRole())
                .email(save.getEmail())
                .age(save.getAge())
                .fullName(save.getFullName())
                .build();
    }

    public UserInfoView findByEmail(UserFindByEmailDto dto) {
        if (!dto.getCurrentUserRole().equals("ADMIN")) {
            throw new UnAuthorizedException("User doesn't have the authority for this operation", HttpStatus.valueOf(401));
        }
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException("User with such email does not exist", HttpStatus.BAD_REQUEST));


        return UserInfoView.builder()
                .id(userEntity.id)
                .role(userEntity.getRole())
                .email(userEntity.getEmail())
                .age(userEntity.getAge())
                .fullName(userEntity.getFullName())
                .build();
    }

    public UserEntity findUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User with such email does not exist", HttpStatus.BAD_REQUEST));

        return userEntity;
    }

    public UserInfoView changeRole(UserChangeRoleDto dto) {
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException("User with such email does not exist", HttpStatus.BAD_REQUEST));

        if (!dto.getRole().equals("ADMIN")) {
            throw new UnAuthorizedException("User doesn't have the authority for this operation", HttpStatus.valueOf(401));
        }

        if (!dto.getChangeUserRole().toUpperCase().equals(RoleEnum.ADMIN.name()) &&
                !dto.getChangeUserRole().toUpperCase().equals(RoleEnum.USER.name())) {
            throw new UserException("Role is not valid", HttpStatus.BAD_REQUEST);
        }

        userEntity.setRole(RoleEnum.valueOf(dto.getChangeUserRole().toUpperCase()));
        UserEntity savedEntity = userRepository.save(userEntity);

        return UserInfoView.builder()
                .id(savedEntity.id)
                .role(savedEntity.getRole())
                .email(savedEntity.getEmail())
                .age(savedEntity.getAge())
                .fullName(savedEntity.getFullName())
                .build();
    }

    public void like(UserEntity userByEmail, Like savedLike) {
        userByEmail.getLikes().add(savedLike);
        userRepository.save(userByEmail);
    }

    public void unlike(UserEntity userByEmail) {
        userRepository.save(userByEmail);
    }


    public boolean forgottenPassword(UserEmailDto dto, Locale preferredLang) {

        UserEntity userEntity = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new UnAuthorizedException("Such email does not exist", HttpStatus.valueOf(401)));
        String newPassword = UUID.randomUUID().toString().substring(0, 10);
        userEntity.setPass(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        ForgottenPasswordEmailMessageEvent event = new ForgottenPasswordEmailMessageEvent(this)
                .setEmail(dto.getUserEmail()).setPassword(newPassword).setLocale(preferredLang);

        publisher.publishEvent(event);

        return true;
    }

    @Override
    public void banUser(UserEmailDto userEmailDto) {
        UserEntity userByEmail = findUserByEmail(userEmailDto.getUserEmail());

        if (userByEmail.getIpAddress().equals("AdminIp")) {
            log.info("Admin tried to ban Head admin !!!");
            throw new UserException("Head admin cannot be banned!", HttpStatus.valueOf(403));
        }

        blackListService.addToBlackList(userByEmail.getIpAddress());
    }


}
