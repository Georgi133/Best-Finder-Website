package softuni.WebFinderserver.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.jwt.JwtService;
import softuni.WebFinderserver.model.dtos.UserDto;
import softuni.WebFinderserver.model.dtos.UserLoginDto;
import softuni.WebFinderserver.model.dtos.UserRegistrationDto;
import softuni.WebFinderserver.model.entities.Role;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserRegisterView register(UserRegistrationDto request) {

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
    public UserLoginView login(UserLoginDto request) {authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        var jwtToken = jwtService.generateToken(user);

        return UserLoginView.builder()
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .age(user.getAge())
                .build();
    }


}
