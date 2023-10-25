package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Game;
import softuni.WebFinderserver.model.views.AnimeCreateView;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.GameCreateView;
import softuni.WebFinderserver.repositories.GameRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;

    public GameService(GameRepository gameRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner) {
        this.gameRepository = gameRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
    }
    public BaseView createGame (TorrentUploadDto dto, MultipartFile file) throws IOException {
        Optional<Game> game = gameRepository.findFirstByGameName(dto.getTorrentName());
        if(game.isPresent()) {
            throw new RuntimeException("Such game already exist");
        }
        Game mappedGameToBeSaved = mapToGame(dto, file);
        Game savedGame = gameRepository.save(mappedGameToBeSaved);

        return mapToView(savedGame);
    }
    private GameCreateView mapToView(Game savedGame) {
        GameCreateView build = GameCreateView.builder()
                .resume(savedGame.getResume())
                .gameName(savedGame.getGameName())
                .releasedYear(savedGame.getReleasedYear())
                .addedDate(savedGame.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedGame.getPictureUrl())
                .build();
        build.setId(savedGame.getId());

        return build;
    }
    private Game mapToGame(TorrentUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Game game = new Game();
        game.setGameName(dto.getTorrentName());
        game.setLikes(new ArrayList<>());
        game.setComments(new ArrayList<>());
        game.setResume(dto.getTorrentResume());
        game.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        game.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        game.setAddedDate(LocalDate.now());
        game.setReleasedYear(dto.getReleasedYear());

        return game;
    }


}
