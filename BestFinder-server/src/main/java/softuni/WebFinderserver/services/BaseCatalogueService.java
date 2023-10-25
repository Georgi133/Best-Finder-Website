package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.views.BaseView;

import java.io.IOException;


@Service
public class BaseCatalogueService {

    private final GameService gameService;
    private final JokeService jokeService;
    private final SongService songService;
    private final SerialService serialService;
    private final MovieService movieService;
    private final AnimeService animeService;

    public BaseCatalogueService(GameService gameService, JokeService jokeService, SongService songService, SerialService serialService, MovieService movieService, AnimeService animeService) {
        this.gameService = gameService;
        this.jokeService = jokeService;
        this.songService = songService;
        this.serialService = serialService;
        this.movieService = movieService;
        this.animeService = animeService;
    }

    public BaseView createTorrent(TorrentUploadDto dto, MultipartFile file) throws IOException {
        return torrentServiceInterceptor(dto.getTorrent(), dto, file);
    }
    private BaseView torrentServiceInterceptor(String torrent, TorrentUploadDto dto, MultipartFile file) throws IOException {
        BaseView baseView = new BaseView();
        if(torrent.equals("movie")) {
            baseView = movieService.createMovie(dto, file);
        }else if (torrent.equals("anime")) {
            baseView = animeService.createAnime(dto, file);
        } else if (torrent.equals("joke")) {
            baseView = jokeService.createJoke(dto, file);
        } else if (torrent.equals("serial")) {
            baseView = serialService.createSerial(dto, file);
        } else if (torrent.equals("game")) {
            baseView = gameService.createGame(dto, file);
        } else if (torrent.equals("song")) {
            baseView = songService.createSong(dto, file);
        }
        return baseView;
    }



}
