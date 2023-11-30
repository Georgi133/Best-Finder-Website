package softuni.WebFinderserver.services.proxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.MovieUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;
import softuni.WebFinderserver.services.businessServices.MovieServiceImpl;
import softuni.WebFinderserver.services.businessServicesInt.MovieService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Qualifier("MovieProxy")
public class MovieServiceProxy implements MovieService {

    private TorrentInfoView bgInfo;
    private List<BaseView> ordinaryTorrentCollection;
    private List<BaseView> ordinaryTorrentCollectionByYear;
    private TorrentInfoView enInfo;
    private Thread thread;

    @Autowired
    private MovieServiceImpl service;

    @Override
    public BaseView createMovie(MovieUploadDto dto, MultipartFile file) throws IOException {
        if(bgInfo != null) {
            bgInfo = null;
        }
        if(enInfo != null) {
            enInfo = null;
        }

        if (ordinaryTorrentCollection != null || ordinaryTorrentCollectionByYear!= null) {
            ordinaryTorrentCollection = null;
            ordinaryTorrentCollectionByYear = null;
        }
        BaseView movie = service.createMovie(dto, file);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
               getAll();
               sortByYear();
            }
        });

        thread.start();
        return movie;
    }

    @Override
    public List<BaseView> sortByYear() {
        if (ordinaryTorrentCollectionByYear == null) {
            ordinaryTorrentCollectionByYear = service.sortByYear();
            return ordinaryTorrentCollectionByYear;
        }
        return ordinaryTorrentCollectionByYear;
    }

    @Override
    public BaseView getById(Long id, String userEmail) {
        return service.getById(id, userEmail);
    }

    @Override
    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        if(ordinaryTorrentCollectionByYear != null) {
            ordinaryTorrentCollectionByYear = null;
        }
        return service.uploadCommentByMovieId(id, dto);
    }

    @Override
    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        if(ordinaryTorrentCollectionByYear != null) {
            ordinaryTorrentCollectionByYear = null;
        }
        return service.deleteCommentById(animeId, commentId, userEmail);
    }

    @Override
    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        if(ordinaryTorrentCollectionByYear != null) {
            ordinaryTorrentCollectionByYear = null;
        }
        return service.editCommentById(animeId, commentId, dto);
    }

    @Override
    public BaseView like(Long id, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        if(ordinaryTorrentCollectionByYear != null) {
            ordinaryTorrentCollectionByYear = null;
        }
        return service.like(id, userEmail);
    }

    @Override
    public BaseView unlike(Long id, String userEmail) {
        if(ordinaryTorrentCollection != null) {
            ordinaryTorrentCollection = null;
        }
        if(ordinaryTorrentCollectionByYear != null) {
            ordinaryTorrentCollectionByYear = null;
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
    public List<BaseView> getAllByCriteriaSortedByLikes(String criteria) {
        return service.getAllByCriteriaSortedByLikes(criteria);
    }

    @Override
    public List<BaseView> getAllByCriteriaSortedByYear(String criteria) {
        return service.getAllByCriteriaSortedByYear(criteria);
    }


}
