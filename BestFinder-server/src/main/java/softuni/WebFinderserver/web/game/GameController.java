package softuni.WebFinderserver.web.game;

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
import softuni.WebFinderserver.services.businessServicesInt.GameService;
import softuni.WebFinderserver.services.businessServices.GameServiceImpl;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-game", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto", required = false)@Valid GameAnimeUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(gameService.createGame(dto, file));
    }

    @GetMapping(value = "/get-all/games")
    public ResponseEntity<?> getAll() {

        List<BaseView> games = gameService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(games);
    }

    @PostMapping(value = "/get-all/games/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = gameService.getAllByCriteriaSortedByYear(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @PostMapping(value = "/get-all/games/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = gameService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @GetMapping(value = "/games/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> games = gameService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(games);
    }

    @PostMapping(value = "/get/game/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = gameService.getById(id, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/upload/game/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView anime = gameService.uploadCommentByMovieId(id, dto);

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @DeleteMapping(value = "/delete/game/{gameId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long gameId,
                                                        @PathVariable Long commentId,
                                                        @RequestBody UserEmailDto dto) {

        BaseView anime = gameService.deleteCommentById(gameId, commentId, dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PatchMapping(value = "/edit/game/{gameId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long gameId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = gameService.editCommentById(gameId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/game/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @RequestBody UserEmailDto dto) {

        BaseView anime = gameService.like(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @PostMapping(value = "/game/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id, @RequestBody UserEmailDto dto) {

        BaseView anime = gameService.unlike(id ,dto.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @GetMapping(value = "/game-info")
    public ResponseEntity<?> getInfo (ServletWebRequest request) {
        TorrentInfoView categoryInfo = gameService.getCategoryInfo(request.getLocale());

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
