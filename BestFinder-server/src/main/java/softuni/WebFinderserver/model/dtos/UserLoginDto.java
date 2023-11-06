package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @NotBlank(message = "Shouldn't be empty")
    @Email
    private String email;

    @NotNull
    @Size(min = 4, message = "Password must contain minimum 4 characters")
    @Size(max = 20, message = "Password must be maximum 20 characters")
    private String password;


}
