package softuni.WebFinderserver.model.views;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MovieCreateView extends BaseView{

    private String torrent;
    private String pictureUrl;
    private String addedDate;
    private String movieName;
    private String resume;
    private Integer releasedYear;
    private String actors;
    private String categories;
    private boolean isLikedByUser;
    private Integer countLikes;
    private List<CommentView> comments;
    private List<LikeView> likes;
    private String videoUrl;

    public MovieCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }

}
