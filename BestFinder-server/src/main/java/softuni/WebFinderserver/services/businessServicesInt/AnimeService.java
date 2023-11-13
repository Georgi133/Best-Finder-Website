package softuni.WebFinderserver.services.businessServicesInt;

import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.CommentEditDto;
import softuni.WebFinderserver.model.dtos.CommentUploadDto;
import softuni.WebFinderserver.model.dtos.GameAnimeUploadDto;
import softuni.WebFinderserver.model.views.AnimeCreateView;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.TorrentInfoView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public interface AnimeService {

    public AnimeCreateView createAnime(GameAnimeUploadDto dto, MultipartFile file) throws IOException;
    public List<BaseView> sortByYear() ;

    public BaseView getById(Long id, String userEmail);

    public BaseView uploadCommentByAnimeId(Long id, CommentUploadDto dto);

    public BaseView deleteCommentById(Long animeId, Long commentId, String userEmail);

    public BaseView editCommentById(Long animeId, Long commentId, CommentEditDto dto);

    public BaseView like(Long id, String userEmail);

    public BaseView unlike(Long id, String userEmail);

    public TorrentInfoView getCategoryInfo(Locale lang) ;

    public List<BaseView> getAll();

    List<BaseView> getAllByCriteriaSortedByYear(String searchBar);

    List<BaseView> getAllByCriteriaSortedByLikes(String searchBar);
}
