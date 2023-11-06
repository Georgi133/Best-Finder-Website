package softuni.WebFinderserver.model.views;

import jakarta.persistence.Column;
import lombok.*;
import softuni.WebFinderserver.model.entities.Singer;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongCreateView extends BaseView{

    private String  addedDate;
    private String pictureUrl;
    private String songName;
    private String singers;
    private Integer releasedYear;
    private String categories;
    private String torrent;

    private boolean isLikedByUser;
    private Integer countLikes;
    private List<CommentView> comments;
    private List<LikeView> likes;
    private String videoUrl;

    public SongCreateView setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
        return this;
    }
}
