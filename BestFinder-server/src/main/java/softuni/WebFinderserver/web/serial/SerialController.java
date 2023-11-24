package softuni.WebFinderserver.web.serial;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.UserEntityClone;
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
@Tag(name = "Serial")
public class SerialController {

    private final SerialService serialService;

    public SerialController(@Qualifier("SerialProxy") SerialService serialService) {
        this.serialService = serialService;
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-serial", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid SerialUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(serialService.createSerial(dto, file));
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all serials")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all serials are delivered"),
            }
    )
    @GetMapping(value = "/get-all/serials")
    public ResponseEntity<?> getAll() {

        List<BaseView> serials = serialService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(serials);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all serials filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all serials are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
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

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get serial by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If serial is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If serial does not exist"),
            }
    )
    @GetMapping(value = "/get/serial/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        BaseView anime = serialService.getById(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to serial")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If serial does not exist"),
            }
    )
    @PostMapping(value = "/upload/serial/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = serialService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.CREATED).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from serial")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If serial does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/serial/{serialId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long serialId,
                                                        @PathVariable Long commentId) {

        BaseView anime = serialService.deleteCommentById(serialId, commentId, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit serial's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If serial does not exist"),
            }
    )
    @PatchMapping(value = "/edit/serial/{serialId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long serialId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = serialService.editCommentById(serialId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the serial")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If serial is liked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If serial does not exist"),
            }
    )
    @PostMapping(value = "/serial/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView anime = serialService.like(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the serial")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If serial is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If serial does not exist"),
            }
    )
    @PostMapping(value = "/serial/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = serialService.unlike(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get serial category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
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
