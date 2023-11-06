package softuni.WebFinderserver.model.views;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentView {

    private Long id;
    private String fullName;
    private String comment;
    private String userEmail;
}
