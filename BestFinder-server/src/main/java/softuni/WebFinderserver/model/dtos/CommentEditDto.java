package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentEditDto {

    @NotNull
    @Size(min = 1)
    private String comment;

}
