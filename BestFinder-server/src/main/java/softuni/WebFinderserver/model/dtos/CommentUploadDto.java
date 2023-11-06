package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUploadDto {
    @NotNull
    @Size(min = 1, message = "Must contain at least 1 character!")
    private String comment;
    @NotNull
    private String userEmail;

}
