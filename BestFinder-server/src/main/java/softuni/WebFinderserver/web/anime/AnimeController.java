package softuni.WebFinderserver.web.anime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
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
import softuni.WebFinderserver.services.businessServicesInt.AnimeService;
import softuni.WebFinderserver.services.businessServices.AnimeServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(@Qualifier("AnimeProxy") AnimeService animeService) {
        this.animeService = animeService;
    }


//    @Operation(summary = "Create dog", security = {
//            @SecurityRequirement(name = "Authorization")})
//    @ApiResponses(
//            value = {@ApiResponse(responseCode = "201", description = "Dog was register.",
//                    content = {@Content(mediaType = "multipart/form-data",
//                            schema = @Schema(implementation = GameAnimeUploadDto.class))}),
//                    @ApiResponse(responseCode = "409", description = "Some fields were incorrect."),
//                    @ApiResponse(responseCode = "401", description = "User has no privileges as an ADMIN or MODERATOR or MEMBER.")}
//    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-anime", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadAnime(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid GameAnimeUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(animeService.createAnime(dto, file));
    }

    @GetMapping(value = "/get-all/animes")
    public ResponseEntity<?> getAll() {

        List<BaseView> animes = animeService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(animes);
    }

    @PostMapping(value = "/get-all/animes/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = animeService.getAllByCriteriaSortedByYear(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @PostMapping(value = "/get-all/animes/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = animeService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @PostMapping(value = "/get/anime/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = animeService.getById(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/upload/anime/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = animeService.uploadCommentByAnimeId(id, dto);

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }


    @DeleteMapping(value = "/delete/anime/{animeId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long animeId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody UserEmailDto dto) {

        BaseView anime = animeService.deleteCommentById(animeId, commentId, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PatchMapping(value = "/edit/anime/{animeId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long animeId,
                                                        @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = animeService.editCommentById(animeId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/anime/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                                      @RequestBody UserEmailDto dto) {

        BaseView anime = animeService.like(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/anime/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = animeService.unlike(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/anime-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = animeService.getCategoryInfo(request.getLocale());

        return ResponseEntity.
                status(HttpStatus.OK).body(categoryInfo);
    }


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
