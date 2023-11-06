package softuni.WebFinderserver.model.views;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Builder
@Service
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnimeCreateView extends BaseView {

    private Integer countLikes;
    private List<CommentView> comments;
    private String pictureUrl;
    private boolean isLikedByUser;
    private String addedDate;
    private String animeName;
    private Integer releasedYear;
    private String resume;
    private String categories;
    private String torrent;
    private String videoUrl;
    private List<LikeView> likes;


    public AnimeCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }

}
