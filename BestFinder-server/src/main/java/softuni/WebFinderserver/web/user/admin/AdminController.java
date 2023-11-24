package softuni.WebFinderserver.web.user.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.UserInfoView;

import softuni.WebFinderserver.services.businessServices.UserServiceImpl;
import softuni.WebFinderserver.services.businessServicesInt.UserService;


@RestController
@RequestMapping("/admins")
@Tag(name = "Admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Find User by email")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserFindByEmailDto.class))),
                    @ApiResponse(responseCode = "401", description = "User has no privileges for this operation"),
                    @ApiResponse(responseCode = "400", description = "User with such email does not exist"),
            }
    )
    @PostMapping("/find-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserInfoView> findUserByEmail (@RequestBody @Valid UserFindByEmailDto dto) {

            UserInfoView view =  userService.findByEmail(dto);
            return ResponseEntity.ok(view);
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Change user role")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User role changed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserChangeRoleDto.class))),
                    @ApiResponse(responseCode = "401", description = "User has no privileges for this operation"),
                    @ApiResponse(responseCode = "400", description = "User with such email does not exist")
            }
    )
    @PatchMapping("/change")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserInfoView> changeUserRole(@RequestBody UserChangeRoleDto dto) {

            UserInfoView view = userService.changeRole(dto);
            return ResponseEntity.ok(view);

    }


    @Operation(summary = "Ban user", hidden = true)
    @PostMapping("/ban-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> banUser (@RequestBody UserEmailDto userEmailDto) {
        userService.banUser(userEmailDto);

       return ResponseEntity.status(HttpStatus.OK).body(new SuccessDto(true));
    }


}
