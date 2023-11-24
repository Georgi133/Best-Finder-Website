package softuni.WebFinderserver.web.joke;

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
import softuni.WebFinderserver.services.businessServicesInt.JokeService;
import softuni.WebFinderserver.services.businessServices.JokeServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Joke")
public class JokeController {
    private  final JokeService jokeService;

    public JokeController(@Qualifier("JokeProxy") JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-joke", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file" , required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid JokeUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(jokeService.createJoke(dto, file));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all jokes")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all jokes are delivered"),
            }
    )
    @GetMapping(value = "/get-all/jokes")
    public ResponseEntity<?> getAll() {

        List<BaseView> jokes = jokeService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(jokes);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all jokes filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all jokes are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/jokes/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = jokeService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get joke by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If joke is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If joke does not exist"),
            }
    )
    @GetMapping(value = "/get/joke/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        BaseView anime = jokeService.getById(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to joke")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If joke does not exist"),
            }
    )
    @PostMapping(value = "/upload/joke/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = jokeService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.CREATED).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from joke")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If joke does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/joke/{jokeId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long jokeId,
                                                        @PathVariable Long commentId) {
        BaseView anime = jokeService.deleteCommentById(jokeId, commentId, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit joke's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If joke does not exist"),
            }
    )
    @PatchMapping(value = "/edit/joke/{jokeId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long jokeId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = jokeService.editCommentById(jokeId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the joke")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If joke is liked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If joke does not exist"),
            }
    )
    @PostMapping(value = "/joke/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView anime = jokeService.like(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the joke")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If joke is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If joke does not exist"),
            }
    )
    @PostMapping(value = "/joke/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = jokeService.unlike(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get joke category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
    @GetMapping(value = "/joke-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = jokeService.getCategoryInfo(request.getLocale());

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
