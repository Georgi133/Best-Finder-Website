package softuni.WebFinderserver.web.movie;

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
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServicesInt.MovieService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;
import softuni.WebFinderserver.model.UserEntityClone;

import java.io.IOException;


@RestController
@Tag(name = "Movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(@Qualifier("MovieProxy") MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-movie", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadMovie(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto") @Valid MovieUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(movieService.createMovie(dto, file));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all movies")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all movies are delivered"),
            }
    )
    @GetMapping(value = "/get-all/movies")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAll());
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all movies filtered by year and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all movies are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/movies/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAllByCriteriaSortedByYear(dto.getSearchBar()));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all movies filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all movies are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/movies/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAllByCriteriaSortedByLikes(dto.getSearchBar()));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all movies filtered by year only")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all movies are delivered"),
            }
    )
    @GetMapping(value = "/movies/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.sortByYear());
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get movie by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If movie is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If movie does not exist"),
            }
    )
    @GetMapping(value = "/get/movie/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getById(id, UserEntityClone.getUserEmail()));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to movie")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If movie does not exist"),
            }
    )
    @PostMapping(value = "/upload/movie/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(movieService.uploadCommentByMovieId(id, dto));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from movie")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If movie does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long movieId,
                                           @PathVariable Long commentId) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.deleteCommentById(movieId, commentId, UserEntityClone.getUserEmail()));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit movie's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If movie does not exist"),
            }
    )
    @PatchMapping(value = "/edit/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long movieId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {
        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.editCommentById(movieId, commentId, dto));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the movie")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If movie is liked"),
                    @ApiResponse(responseCode = "400", description = "If movie does not exist"),
            }
    )
    @PostMapping(value = "/movie/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView like = movieService.like(id, UserEntityClone.getUserEmail());
        return ResponseEntity.
                status(HttpStatus.OK).body(like);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the movie")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If movie is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If movie does not exist"),
            }
    )
    @PostMapping(value = "/movie/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = movieService.unlike(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/movie-info")
    public ResponseEntity<?> getInfo(ServletWebRequest request) {
        TorrentInfoView categoryInfo = movieService.getCategoryInfo(request.getLocale());

        return ResponseEntity.
                status(HttpStatus.OK).body(categoryInfo);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get movie category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
    @ExceptionHandler({UploadTorrentException.class, TorrentException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(TorrentException ex) {

        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage())
                        .build());
    }


}
