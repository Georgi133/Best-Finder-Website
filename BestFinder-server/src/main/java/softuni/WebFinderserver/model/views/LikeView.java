package softuni.WebFinderserver.model.views;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeView implements Serializable {

    private Long likeId;
    private Long torrentId;
    private String userEmail;

}
