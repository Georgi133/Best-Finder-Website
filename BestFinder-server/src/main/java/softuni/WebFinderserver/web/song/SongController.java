package softuni.WebFinderserver.web.song;

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
import softuni.WebFinderserver.services.businessServicesInt.SongService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Song")
public class SongController {

    private final SongService songService;

    public SongController(@Qualifier("SongProxy") SongService songService) {
        this.songService = songService;
    }


    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-song", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto") @Valid SongUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(songService.createSong(dto, file));
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all songs")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all songs are delivered"),
            }
    )
    @GetMapping(value = "/get-all/songs")
    public ResponseEntity<?> getAll() {

        List<BaseView> songs = songService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(songs);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all songs filtered by year and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all songs are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/songs/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = songService.getAllByCriteriaSortedByYear(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all songs filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all songs are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/songs/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = songService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all songs filtered by year only")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all songs are delivered"),
            }
    )
    @GetMapping(value = "/songs/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> songs = songService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(songs);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get song by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If song is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If song does not exist"),
            }
    )
    @GetMapping(value = "/get/song/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        BaseView anime = songService.getById(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to song")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If song does not exist"),
            }
    )
    @PostMapping(value = "/upload/song/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView song = songService.uploadCommentByAnimeId(id, dto);

        return ResponseEntity.
                status(HttpStatus.CREATED).body(song);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from song")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If song does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/song/{songId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long songId,
                                                        @PathVariable Long commentId) {

        BaseView anime = songService.deleteCommentById(songId, commentId, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit song's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If song does not exist"),
            }
    )
    @PatchMapping(value = "/edit/song/{songId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long songId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = songService.editCommentById(songId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }


    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the song")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If song is liked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If song does not exist"),
            }
    )
    @PostMapping(value = "/song/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView anime = songService.like(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the song")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If song is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If song does not exist"),
            }
    )
    @PostMapping(value = "/song/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = songService.unlike(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get song category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
    @GetMapping(value = "/song-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = songService.getCategoryInfo(request.getLocale());

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
