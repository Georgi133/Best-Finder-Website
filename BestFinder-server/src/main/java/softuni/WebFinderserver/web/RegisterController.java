package softuni.WebFinderserver.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import softuni.WebFinderserver.model.dtos.UserRegistrationDto;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.services.UserService;

@RequestMapping("/register")
@RestController
@CrossOrigin("*")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserRegisterView> register (
            @RequestBody @Valid UserRegistrationDto userRegistrationDto
    , UriComponentsBuilder uriComponentsBuilder) {

        UserRegisterView userRegisterView = userService.create(userRegistrationDto);
        if(userRegisterView == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(userRegisterView);
    }


}
