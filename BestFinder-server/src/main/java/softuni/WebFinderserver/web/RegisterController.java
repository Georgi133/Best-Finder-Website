package softuni.WebFinderserver.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softuni.WebFinderserver.model.dtos.UserRegistrationDto;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.services.UserService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class RegisterController {

    private final UserService userService;


    @PostMapping(path = "/register")
    public ResponseEntity<UserRegisterView> register (
            @RequestBody UserRegistrationDto userRegistrationDto) {

        UserRegisterView register = userService.register(userRegistrationDto);

     return ResponseEntity.ok(register);
    }



}
