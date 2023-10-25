package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {


    @NotNull
    private String fullName;
    @NotNull
    private Integer age;
    @Email
    @NotNull
    private String email;
    @Size(min = 3)
    @NotNull
    private String password;

}