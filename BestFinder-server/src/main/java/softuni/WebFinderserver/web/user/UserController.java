package softuni.WebFinderserver.web.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.services.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/change-password")
    public ResponseEntity<SuccessDto>changePassword(@RequestBody UserChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok(new SuccessDto().setCreated(true));
    }

    @PostMapping("/get-user")
    public ResponseEntity<UserInfoView>changePassword(@RequestBody UserChangeProfileDto userDto) {
       UserInfoView view =  userService.getUserByEmail(userDto.getEmail());
        return ResponseEntity.ok(view);
    }

    @PatchMapping("/edit-profile")
    public ResponseEntity<UserInfoView>editProfile(@RequestBody UserEditProfileDto userEditProfileDto) {

        UserInfoView view = userService.editProfile(userEditProfileDto);

        return ResponseEntity.ok(view);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserRegisterView> register (
            @RequestBody @Valid UserRegistrationDto userRegistrationDto) {

        UserRegisterView register = userService.register(userRegistrationDto);

        return ResponseEntity.ok(register);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginView> login (
            @RequestBody UserLoginDto userLoginDto) {
        UserLoginView login = userService.login(userLoginDto);

        return ResponseEntity.ok(login);
    }


}
