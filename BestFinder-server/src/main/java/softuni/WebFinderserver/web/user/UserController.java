package softuni.WebFinderserver.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Change password")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Password changed",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserChangePasswordDto.class))),
                    @ApiResponse(responseCode = "401", description = "Email or passwords are not correct")
            }
    )
    @PatchMapping("/change-password")
    public ResponseEntity<?>changePassword(@RequestBody @Valid UserChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto().setCreated(true));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserChangeProfileDto.class))),
                    @ApiResponse(responseCode = "400", description = "User with such email doesn't exist")
            }
    )
    @PostMapping("/get-user")
    public ResponseEntity<?>getUser(@RequestBody UserChangeProfileDto userDto) {
       UserInfoView view =  userService.getUserByEmail(userDto.getEmail());
        return ResponseEntity.ok(view);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit profile")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Profile edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserEditProfileDto.class))),
                    @ApiResponse(responseCode = "401", description = "User with such email doesn't exist"),
            }
    )
    @PatchMapping("/edit-profile")
    public ResponseEntity<UserInfoView>editProfile(@RequestBody @Valid UserEditProfileDto userEditProfileDto) {

        UserInfoView view = userService.editProfile(userEditProfileDto);

        return ResponseEntity.ok(view);
    }


    @Operation(summary = "Send new password")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Password sent to the given email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserEmailDto.class))),
                    @ApiResponse(responseCode = "401", description = "Such email does not exist"),
            }
    )
    @PostMapping("forgotten/password")
    public ResponseEntity<?>forgottenPasswordEmail(@RequestBody @Valid UserEmailDto dto, ServletWebRequest request) throws MessagingException, UnsupportedEncodingException {

        boolean isChanged = userService.forgottenPassword(dto, request.getLocale());

        return ResponseEntity.status(200).body(new SuccessDto(isChanged));
    }

    @Operation(summary = "Register")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If registration is successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserRegistrationDto.class))),
                    @ApiResponse(responseCode = "403", description = "User with such email already exists"),
            }
    )
    @PostMapping(path = "/register")
    public ResponseEntity<?> register (
            @RequestBody @Valid UserRegistrationDto userRegistrationDto, HttpServletRequest request) {

        UserRegisterView register = userService.register(userRegistrationDto,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @Operation(summary = "Login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If user is logged",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserLoginDto.class))),
                    @ApiResponse(responseCode = "401", description = "If user is not found"),
            }
    )
    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginView> login (
            @RequestBody @Valid UserLoginDto userLoginDto) {

        UserLoginView login = userService.login(userLoginDto);

        return ResponseEntity.ok(login);
    }


}
