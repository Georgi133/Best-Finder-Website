package softuni.WebFinderserver.services.proxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.SerialUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServices.SerialServiceImpl;
import softuni.WebFinderserver.services.businessServicesInt.SerialService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Qualifier("SerialProxy")
public class SerialServiceProxy implements SerialService {

    @Autowired
    private SerialServiceImpl service;
    private TorrentInfoView bgInfo;
    private List<BaseView> ordinaryTorrentCollection;
    private TorrentInfoView enInfo;
    private Thread thread;

    @Override
    public BaseView createSerial(SerialUploadDto dto, MultipartFile file) throws IOException {
        if(bgInfo != null) {
            bgInfo = null;
        }
        if(enInfo != null) {
            enInfo = null;
        }
        if (ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        BaseView serial = service.createSerial(dto, file);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getAll();
            }
        });
        thread.start();
        return serial;
    }

    @Override
    public List<BaseView> sortBySeasons() {
        return service.sortBySeasons();
    }

    @Override
    public BaseView getById(Long id, String userEmail) {
        return service.getById(id,userEmail);
    }

    @Override
    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        return service.uploadCommentByMovieId(id,dto);
    }

    @Override
    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        return service.deleteCommentById(animeId, commentId, userEmail);
    }

    @Override
    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        return service.editCommentById(animeId, commentId, dto);
    }

    @Override
    public BaseView like(Long id, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        return service.like(id, userEmail);
    }

    @Override
    public BaseView unlike(Long id, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        return service.unlike(id, userEmail);
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
    public List<BaseView> getAllByCriteriaSortedByLikes(String searchBar) {
        return service.getAllByCriteriaSortedByLikes(searchBar);
    }

}
