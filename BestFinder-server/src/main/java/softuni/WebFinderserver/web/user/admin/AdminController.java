package softuni.WebFinderserver.web.user.admin;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import softuni.WebFinderserver.model.dtos.UserChangeRoleDto;
import softuni.WebFinderserver.model.dtos.UserFindByEmailDto;
import softuni.WebFinderserver.model.views.UserInfoView;

import softuni.WebFinderserver.services.UserService;


@RestController
@RequestMapping("/admins")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/find-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserInfoView> findUserByEmail (@RequestBody @Valid UserFindByEmailDto dto) {

            UserInfoView view =  userService.findByEmail(dto);
            return ResponseEntity.ok(view);
    }

    @PatchMapping("/change")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserInfoView> changeUserRole(@RequestBody UserChangeRoleDto dto) {

            UserInfoView view = userService.changeRole(dto);
            return ResponseEntity.ok(view);

    }

}
