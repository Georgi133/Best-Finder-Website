package softuni.WebFinderserver.web.movie;

import jakarta.validation.Valid;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.MovieService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;
import softuni.WebFinderserver.services.exceptions.user.UserException;

import java.io.IOException;
import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-movie", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto", required = false) @Valid MovieUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(movieService.createMovie(dto, file));
    }

    @GetMapping(value = "/get-all/movies")
    public ResponseEntity<?> getAll() {

        List<BaseView> movies = movieService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @GetMapping(value = "/movies/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> movies = movieService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @PostMapping(value = "/get/movie/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = movieService.getById(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/upload/movie/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = movieService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @DeleteMapping(value = "/delete/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> deleteCommentFromAnimeById(@PathVariable Long movieId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody UserEmailDto dto) {

        BaseView anime = movieService.deleteCommentById(movieId, commentId, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PatchMapping(value = "/edit/movie/{movieId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long movieId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = movieService.editCommentById(movieId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/movie/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @RequestBody UserEmailDto dto) {

        BaseView anime = movieService.like(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/movie/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = movieService.unlike(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/movie-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
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
