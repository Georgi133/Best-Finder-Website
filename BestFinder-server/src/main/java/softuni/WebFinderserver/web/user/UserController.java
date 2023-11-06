package softuni.WebFinderserver.web.user;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.services.businessServicesInt.UserService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/change-password")
    public ResponseEntity<?>changePassword(@RequestBody @Valid UserChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto().setCreated(true));
    }

    @PostMapping("/get-user")
    public ResponseEntity<UserInfoView>getUser(@RequestBody UserChangeProfileDto userDto) {
       UserInfoView view =  userService.getUserByEmail(userDto.getEmail());
        return ResponseEntity.ok(view);
    }

    @PatchMapping("/edit-profile")
    public ResponseEntity<UserInfoView>editProfile(@RequestBody @Valid UserEditProfileDto userEditProfileDto) {

        UserInfoView view = userService.editProfile(userEditProfileDto);

        return ResponseEntity.ok(view);
    }

    @PostMapping("forgotten/password")
    public ResponseEntity<?>forgottenPasswordEmail(@RequestBody @Valid UserEmailDto dto, ServletWebRequest request) throws MessagingException, UnsupportedEncodingException {

        boolean isChanged = userService.forgottenPassword(dto, request.getLocale());

        return ResponseEntity.status(200).body(new SuccessDto(isChanged));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register (
            @RequestBody @Valid UserRegistrationDto userRegistrationDto, HttpServletRequest request) {

        UserRegisterView register = userService.register(userRegistrationDto,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginView> login (
            @RequestBody @Valid UserLoginDto userLoginDto) {

        UserLoginView login = userService.login(userLoginDto);

        return ResponseEntity.ok(login);
    }


}
