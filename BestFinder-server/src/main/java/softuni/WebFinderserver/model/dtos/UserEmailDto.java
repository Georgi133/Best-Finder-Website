package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailDto {

    @Email(message = "Email should be valid format!")
    @NotNull
    private String userEmail;

}
