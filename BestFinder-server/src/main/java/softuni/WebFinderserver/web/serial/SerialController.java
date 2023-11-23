package softuni.WebFinderserver.web.serial;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServicesInt.SerialService;
import softuni.WebFinderserver.services.businessServices.SerialServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
public class SerialController {

    private final SerialService serialService;

    public SerialController(@Qualifier("SerialProxy") SerialService serialService) {
        this.serialService = serialService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-serial", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid SerialUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(serialService.createSerial(dto, file));
    }


//    @Operation(summary = "Get all users", security = {
//            @SecurityRequirement(name = "Bearer")})
//    @ApiResponses(
//            value = {@ApiResponse(responseCode = "200", description = "View all users"),
//                    @ApiResponse(responseCode = "401", description = "User has no privileges as an ADMIN.")}
//    )
    @GetMapping(value = "/get-all/serials")
    public ResponseEntity<?> getAll() {

        List<BaseView> serials = serialService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(serials);
    }

    @PostMapping(value = "/get-all/serials/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = serialService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

//    @GetMapping(value = "/serials/sort-by-seasons")
//    public ResponseEntity<?> sortByYear() {
//
//        List<BaseView> serials = serialService.sortBySeasons();
//
//        return ResponseEntity.
//                status(HttpStatus.OK).body(serials);
//    }

    @PostMapping(value = "/get/serial/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = serialService.getById(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/upload/serial/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = serialService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @DeleteMapping(value = "/delete/serial/{serialId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long serialId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody UserEmailDto dto) {

        BaseView anime = serialService.deleteCommentById(serialId, commentId, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PatchMapping(value = "/edit/serial/{serialId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long serialId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = serialService.editCommentById(serialId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/serial/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @RequestBody UserEmailDto dto) {

        BaseView anime = serialService.like(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/serial/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = serialService.unlike(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/serial-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = serialService.getCategoryInfo(request.getLocale());

        return ResponseEntity.
                status(HttpStatus.OK).body(categoryInfo);
    }

    @ExceptionHandler({UploadTorrentException.class, TorrentException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(TorrentException ex) {

        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage())
                        .build());
    }


}
