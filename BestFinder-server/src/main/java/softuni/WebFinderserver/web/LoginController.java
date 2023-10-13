package softuni.WebFinderserver.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softuni.WebFinderserver.model.dtos.UserLoginDto;
import softuni.WebFinderserver.model.views.UserLoginView;
import softuni.WebFinderserver.services.UserService;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class LoginController {

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginView> login (
            @RequestBody UserLoginDto userLoginDto) {
        UserLoginView login = userService.login(userLoginDto);

        return ResponseEntity.ok(login);
    }


}
