package softuni.WebFinderserver.web.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.dtos.UserChangeRoleDto;
import softuni.WebFinderserver.model.dtos.UserFindByEmailDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.UserInfoView;
import softuni.WebFinderserver.services.AnimeService;
import softuni.WebFinderserver.services.BaseCatalogueService;
import softuni.WebFinderserver.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final UserService userService;
    private final AnimeService animeService;

    private final BaseCatalogueService baseCatalogueService;


    public AdminController(UserService userService, AnimeService animeService, BaseCatalogueService baseCatalogueService) {
        this.userService = userService;
        this.animeService = animeService;
        this.baseCatalogueService = baseCatalogueService;
    }

    @PostMapping("/find-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoView> findUserByEmail (@RequestBody UserFindByEmailDto dto) {
        if(dto.getCurrentUserRole().equals("ADMIN")) {
            UserInfoView view =  userService.findByEmail(dto.getEmail());
            return ResponseEntity.ok(view);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/change")
    public ResponseEntity<UserInfoView> changeUserRole(@RequestBody UserChangeRoleDto dto) {
        if(dto.getRole().equals("ADMIN")) {
            UserInfoView view = userService.changeRole(dto.getEmail(),dto.getChangeUserRole());
            return ResponseEntity.ok(view);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping(value = "/upload-torrent", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto", required = false) TorrentUploadDto dto) throws IOException {

        System.out.println();
        return ResponseEntity.
                status(HttpStatus.CREATED).body(baseCatalogueService.createTorrent(dto, file));
    }



}
