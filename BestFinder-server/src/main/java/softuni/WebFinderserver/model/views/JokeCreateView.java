package softuni.WebFinderserver.model.views;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JokeCreateView extends BaseView{

    private String pictureUrl;
    private String addedDate;
    private String jokeName;
    private String text;
    private String torrent;
    private String shortText;
    private Integer countLikes;
    private List<CommentView> comments;
    private List<LikeView> likes;
    private boolean isLikedByUser;

    public JokeCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }

}
