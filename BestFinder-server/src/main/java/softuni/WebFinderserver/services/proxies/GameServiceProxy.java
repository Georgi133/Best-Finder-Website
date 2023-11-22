package softuni.WebFinderserver.services.proxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServices.GameServiceImpl;
import softuni.WebFinderserver.services.businessServicesInt.GameService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Qualifier("GameProxy")
public class GameServiceProxy implements GameService {

    @Autowired
    private GameServiceImpl service;
    private TorrentInfoView bgInfo;
    private List<BaseView> ordinaryTorrentCollection;
    private TorrentInfoView enInfo;
    private Thread thread;

    @Override
    public BaseView createGame(GameAnimeUploadDto dto, MultipartFile file) throws IOException {
        if (ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        BaseView game = service.createGame(dto, file);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getAll();
            }
        });

        thread.start();
        return game;
    }

    @Override
    public List<BaseView> sortByYear() {
        return service.sortByYear();
    }

    @Override
    public BaseView getById(Long id, String userEmail) {
        return service.getById(id, userEmail);
    }

    @Override
    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        return service.uploadCommentByMovieId(id, dto);
    }

    @Override
    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        return service.deleteCommentById(animeId,commentId,userEmail);
    }

    @Override
    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {
        return service.editCommentById(animeId,commentId,dto);
    }

    @Override
    public BaseView like(Long id, String userEmail) {
        return service.like(id, userEmail);
    }

    @Override
    public BaseView unlike(Long id, String userEmail) {
        return service.unlike(id,userEmail);
    }

    @Override
    public TorrentInfoView getCategoryInfo(Locale lang) {
        if (lang.getLanguage().equals("bg") && bgInfo == null) {
            bgInfo = service.getCategoryInfo(lang);
            return bgInfo;
        } else if (lang.getLanguage().equals("en") && enInfo == null) {
            enInfo = service.getCategoryInfo(lang);
            return enInfo;
        }

        if (lang.getLanguage().equals("bg")) {
            return bgInfo;
        } else {
            return enInfo;
        }
    }

    @Override
    public List<BaseView> getAll() {
        if (ordinaryTorrentCollection == null) {
            ordinaryTorrentCollection = service.getAll();
            return ordinaryTorrentCollection;
        }
        return ordinaryTorrentCollection;
    }

    @Override
    public List<BaseView> getAllByCriteriaSortedByYear(String searchBar) {
        return service.getAllByCriteriaSortedByYear(searchBar);
    }

    @Override
    public List<BaseView> getAllByCriteriaSortedByLikes(String searchBar) {
        return service.getAllByCriteriaSortedByLikes(searchBar);
    }


}