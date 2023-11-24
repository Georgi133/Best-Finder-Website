package softuni.WebFinderserver.web.anime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.UserEntityClone;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServicesInt.AnimeService;
import softuni.WebFinderserver.services.businessServices.AnimeServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(@Qualifier("AnimeProxy") AnimeService animeService) {
        this.animeService = animeService;
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-anime", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadAnime(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid GameAnimeUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(animeService.createAnime(dto, file));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all anime")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all animes are delivered"),
            }
    )
    @GetMapping(value = "/get-all/animes")
    public ResponseEntity<?> getAll() {

        List<BaseView> animes = animeService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(animes);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all animes filtered by year and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all animes are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/animes/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = animeService.getAllByCriteriaSortedByYear(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all animes filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all animes are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )

    @PostMapping(value = "/get-all/animes/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = animeService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get anime by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If anime is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If anime does not exist"),
            }
    )
    @GetMapping(value = "/get/anime/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        BaseView anime = animeService.getById(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to anime")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If anime does not exist"),
            }
    )
    @PostMapping(value = "/upload/anime/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = animeService.uploadCommentByAnimeId(id, dto);

        return ResponseEntity.
                status(HttpStatus.CREATED).body(anime);
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from anime")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If anime does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/anime/{animeId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long animeId,
                                                        @PathVariable Long commentId) {

        BaseView anime = animeService.deleteCommentById(animeId, commentId, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit anime's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If anime does not exist"),
            }
    )
    @PatchMapping(value = "/edit/anime/{animeId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long animeId,
                                                        @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = animeService.editCommentById(animeId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the anime")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If anime is liked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If anime does not exist"),
            }
    )
    @PostMapping(value = "/anime/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView anime = animeService.like(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the anime")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If anime is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If anime does not exist"),
            }
    )
    @PostMapping(value = "/anime/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = animeService.unlike(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get anime category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
    @GetMapping(value = "/anime-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = animeService.getCategoryInfo(request.getLocale());

        return ResponseEntity.
                status(HttpStatus.OK).body(categoryInfo);
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all animes filtered by year only")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all animes are delivered"),
            }
    )
    @GetMapping(value = "/animes/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> animes = animeService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(animes);
    }

    @ExceptionHandler({UploadTorrentException.class, TorrentException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(TorrentException ex) {
        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage())
                        .build());
    }



}
