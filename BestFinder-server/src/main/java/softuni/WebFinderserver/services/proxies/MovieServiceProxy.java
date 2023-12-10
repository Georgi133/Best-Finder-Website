package softuni.WebFinderserver.services.proxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private TorrentInfoView enInfo;
    private Thread thread;

    @Autowired
    private MovieServiceImpl service;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView createMovie(MovieUploadDto dto, MultipartFile file) throws IOException {
        if(bgInfo != null) {
            bgInfo = null;
        }
        if(enInfo != null) {
            enInfo = null;
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
    @Cacheable("movieByYear")
    public List<BaseView> sortByYear() {
            return service.sortByYear();
    }

    @Override
    public BaseView getById(Long id, String userEmail) {
        return service.getById(id, userEmail);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView uploadCommentByMovieId(Long id, CommentUploadDto dto) {
        return service.uploadCommentByMovieId(id, dto);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail) {
        return service.deleteCommentById(animeId, commentId, userEmail);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto) {
        return service.editCommentById(animeId, commentId, dto);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView like(Long id, String userEmail) {
        return service.like(id, userEmail);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movieByYear", allEntries = true),
            @CacheEvict(value = "movieByLikes", allEntries = true)
    })
    public BaseView unlike(Long id, String userEmail) {
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
    @Cacheable("movieByLikes")
    public List<BaseView> getAll() {
            return service.getAll();
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
