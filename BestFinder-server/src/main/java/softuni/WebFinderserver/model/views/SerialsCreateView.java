package softuni.WebFinderserver.model.views;

import lombok.*;
import softuni.WebFinderserver.model.entities.CategoryProjection;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialsCreateView extends BaseView{

    private String pictureUrl;
    private String addedDate;
    private String serialName;
    private Integer seasons;
    private String resume;
    private String actors;
    private String categories;
    private String torrent;
    private boolean isLikedByUser;
    private Integer countLikes;
    private List<CommentView> comments;
    private List<LikeView> likes;
    private String videoUrl;

    public SerialsCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }

}
