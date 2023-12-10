package softuni.WebFinderserver.model.views;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentView implements Serializable {

    private Long id;
    private String fullName;
    private String comment;
    private String userEmail;
}
