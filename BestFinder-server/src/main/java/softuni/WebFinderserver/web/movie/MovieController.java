package softuni.WebFinderserver.web.movie;

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

import java.io.IOException;


@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(@Qualifier("MovieProxy") MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-movie", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadMovie(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto", required = false) @Valid MovieUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(movieService.createMovie(dto, file));
    }

    @GetMapping(value = "/get-all/movies")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAll());
    }

    @PostMapping(value = "/get-all/movies/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAllByCriteriaSortedByYear(dto.getSearchBar()));
    }

    @PostMapping(value = "/get-all/movies/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getAllByCriteriaSortedByLikes(dto.getSearchBar()));
    }

    @GetMapping(value = "/movies/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.sortByYear());
    }

    @PostMapping(value = "/get/movie/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.getById(id, dto.getUserEmail()));
    }

    @PostMapping(value = "/upload/movie/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.uploadCommentByMovieId(id, dto));
    }

    @DeleteMapping(value = "/delete/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long movieId,
                                           @PathVariable Long commentId,
                                           @RequestBody UserEmailDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.deleteCommentById(movieId, commentId, dto.getUserEmail()));
    }

    @PatchMapping(value = "/edit/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long movieId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.editCommentById(movieId, commentId, dto));
    }

    @PostMapping(value = "/movie/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @RequestBody UserEmailDto dto) {

        return ResponseEntity.
                status(HttpStatus.OK).body(movieService.like(id, dto.getUserEmail()));
    }

    @PostMapping(value = "/movie/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = movieService.unlike(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/movie-info")
    public ResponseEntity<?> getInfo(ServletWebRequest request) {
        TorrentInfoView categoryInfo = movieService.getCategoryInfo(request.getLocale());

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
