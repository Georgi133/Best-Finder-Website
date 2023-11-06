package softuni.WebFinderserver.model.views;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCreateView extends BaseView{

    private String pictureUrl;
    private String addedDate;
    private String gameName;
    private Integer releasedYear;
    private String resume;
    private String categories;
    private String torrent;
    private boolean isLikedByUser;
    private Integer countLikes;
    private List<CommentView> comments;
    private List<LikeView> likes;
    private String videoUrl;

    public GameCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }

}
