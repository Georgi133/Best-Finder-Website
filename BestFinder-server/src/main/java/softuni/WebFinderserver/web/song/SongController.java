package softuni.WebFinderserver.web.song;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.*;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServicesInt.SongService;
import softuni.WebFinderserver.services.businessServices.SongServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
public class SongController {

    private final SongService songService;

    public SongController(SongServiceImpl songService) {
        this.songService = songService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-song", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto", required = false)@Valid SongUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(songService.createSong(dto, file));
    }

    @GetMapping(value = "/get-all/songs")
    public ResponseEntity<?> getAll() {

        List<BaseView> songs = songService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(songs);
    }

    @GetMapping(value = "/songs/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> songs = songService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(songs);
    }

    @PostMapping(value = "/get/song/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = songService.getById(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/upload/song/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = songService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @DeleteMapping(value = "/delete/song/{songId}/comment/{commentId}")
    public ResponseEntity<?> deleteCommentFromAnimeById(@PathVariable Long songId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody UserEmailDto dto) {

        BaseView anime = songService.deleteCommentById(songId, commentId, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PatchMapping(value = "/edit/song/{songId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long songId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = songService.editCommentById(songId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/song/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @RequestBody UserEmailDto dto) {

        BaseView anime = songService.like(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/song/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = songService.unlike(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

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
