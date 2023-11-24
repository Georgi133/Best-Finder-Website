package softuni.WebFinderserver.web.game;

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
import softuni.WebFinderserver.services.businessServicesInt.GameService;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;
import softuni.WebFinderserver.services.exceptions.torrent.UploadTorrentException;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Game")
public class GameController {

    private final GameService gameService;
    public GameController(@Qualifier("GameProxy") GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(hidden = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload-game", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "dto")@Valid GameAnimeUploadDto dto) throws IOException {

        return ResponseEntity.
                status(HttpStatus.CREATED).body(gameService.createGame(dto, file));
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all games")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all games are delivered"),
            }
    )
    @GetMapping(value = "/get-all/games")
    public ResponseEntity<?> getAll() {

        List<BaseView> games = gameService.getAll();

        return ResponseEntity.
                status(HttpStatus.OK).body(games);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all games filtered by year and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all games are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/games/filtered-by-year")
    public ResponseEntity<?> getAllFilteredByYear(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = gameService.getAllByCriteriaSortedByYear(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all games filtered by likes and criteria")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all games are delivered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TorrentSearchBarDto.class))),
            }
    )
    @PostMapping(value = "/get-all/games/filtered-by-likes")
    public ResponseEntity<?> getAllFilteredByLikes(@RequestBody TorrentSearchBarDto dto) {

        List<BaseView> movies = gameService.getAllByCriteriaSortedByLikes(dto.getSearchBar());

        return ResponseEntity.
                status(HttpStatus.OK).body(movies);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all games filtered by year only")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If all games are delivered"),
            }
    )
    @GetMapping(value = "/games/sort-by-year")
    public ResponseEntity<?> sortByYear() {

        List<BaseView> games = gameService.sortByYear();

        return ResponseEntity.
                status(HttpStatus.OK).body(games);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get game by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If game is delivered",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If game does not exist"),
            }
    )
    @GetMapping(value = "/get/game/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        BaseView anime = gameService.getById(id, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Upload comment to game")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "If comment is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentUploadDto.class))),
                    @ApiResponse(responseCode = "400", description = "If game does not exist"),
            }
    )
    @PostMapping(value = "/upload/game/{id}/comment")
    public ResponseEntity<?> uploadComment(@RequestBody @Valid CommentUploadDto dto, @PathVariable Long id) {

        BaseView game = gameService.uploadCommentByGameId(id, dto);

        return ResponseEntity.
                status(HttpStatus.CREATED).body(game);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Delete comment from song")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "If game does not exist"),
            }
    )
    @DeleteMapping(value = "/delete/game/{gameId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long gameId,
                                                        @PathVariable Long commentId) {

        BaseView anime = gameService.deleteCommentById(gameId, commentId, UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Edit game's comment")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If comment is edited",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEditDto.class))),
                    @ApiResponse(responseCode = "403", description = "If game does not exist"),
            }
    )
    @PatchMapping(value = "/edit/game/{gameId}/comment/{commentId}")
    public ResponseEntity<?> editCommentFromAnimeById(@PathVariable Long gameId,
                                                      @PathVariable Long commentId,
                                                      @RequestBody CommentEditDto dto) {

        BaseView anime = gameService.editCommentById(gameId, commentId ,dto);
        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Like the game")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If game is liked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If game does not exist"),
            }
    )
    @PostMapping(value = "/game/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {

        BaseView anime = gameService.like(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Unlike the game")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If game is unliked",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "If game does not exist"),
            }
    )
    @PostMapping(value = "/game/{id}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long id) {

        BaseView anime = gameService.unlike(id ,UserEntityClone.getUserEmail());

        return ResponseEntity.
                status(HttpStatus.OK).body(anime);
    }

    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get game category information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "If info is delivered"),
            }
    )
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
