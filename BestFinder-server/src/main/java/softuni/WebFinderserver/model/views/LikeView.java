package softuni.WebFinderserver.model.views;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeView {

    private Long likeId;
    private Long torrentId;
    private String userEmail;

}
