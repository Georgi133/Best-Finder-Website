package softuni.WebFinderserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.jwt.JwtService;
import softuni.WebFinderserver.model.dtos.UserChangePasswordDto;
import softuni.WebFinderserver.model.dtos.UserEditProfileDto;
import softuni.WebFinderserver.model.dtos.UserLoginDto;
import softuni.WebFinderserver.model.dtos.UserRegistrationDto;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.repositories.UserRepository;
import softuni.WebFinderserver.services.exceptions.SuchUserExistClassException;
import softuni.WebFinderserver.services.exceptions.UserDoesNotExistException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserRegisterView register(UserRegistrationDto request) {
        if(isUserExist(request.getEmail())) {
            throw new SuchUserExistClassException("There is user with such email",  HttpStatus.BAD_REQUEST);
        }

        var user = UserEntity.builder()

                .email(request.getEmail())
                .pass(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.USER)
                .fullName(request.getFullName())
                .age(request.getAge())
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

    public UserLoginView login(UserLoginDto request) {authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken (
                    request.getEmail(),
                    request.getPassword()
            )
    );
        // TODO check if there is no Such email if throw an error or will throw earlier something else
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserDoesNotExistException("User with the given email does not exist!", HttpStatus.BAD_REQUEST));

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

    public void changePassword(UserChangePasswordDto dto) {
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email of user is not valid when changing password"));
        if(dto.getNewPassword().equals(dto.getConfirmPassword())) {
            if (arePasswordsMatching(dto.getCurrentPassword(), userEntity.getPassword())) {
                userEntity.setPass(passwordEncoder.encode(dto.getNewPassword()));
            }else {
                throw new RuntimeException("The given current password is not correct");
            }
        } else {
            throw new RuntimeException("New password and confirmed password don't match");
        }
        userRepository.save(userEntity);
        // TODO : password except
    }

    public boolean arePasswordsMatching(String rawPassword, String userPassword) {
       return passwordEncoder.matches(rawPassword,userPassword);
    }

    public UserInfoView getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email of user not valid when fetching data for profile change"));

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
                .orElseThrow(() -> new UserDoesNotExistException("User with such email does not exist on editing profile", HttpStatus.BAD_REQUEST));
        userEntity.setEmail(userEditProfileDto.getNewEmail());
        userEntity.setAge(userEditProfileDto.getAge());
        userEntity.setFullName(userEditProfileDto.getFullName());

        UserEntity save = userRepository.save(userEntity);

        return  UserInfoView.builder()
                .id(save.id)
                .role(save.getRole())
                .email(save.getEmail())
                .age(save.getAge())
                .fullName(save.getFullName())
                .build();
    }

    public UserInfoView findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExistException("user with such email does not exist", HttpStatus.BAD_REQUEST));


        return  UserInfoView.builder()
                .id(userEntity.id)
                .role(userEntity.getRole())
                .email(userEntity.getEmail())
                .age(userEntity.getAge())
                .fullName(userEntity.getFullName())
                .build();
    }

    public UserInfoView changeRole(String email, String changeUserRole) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExistException("user with such email does not exist", HttpStatus.BAD_REQUEST));

        userEntity.setRole(RoleEnum.valueOf(changeUserRole));
        UserEntity savedEntity = userRepository.save(userEntity);

        return  UserInfoView.builder()
                .id(savedEntity.id)
                .role(savedEntity.getRole())
                .email(savedEntity.getEmail())
                .age(savedEntity.getAge())
                .fullName(savedEntity.getFullName())
                .build();
    }
}
