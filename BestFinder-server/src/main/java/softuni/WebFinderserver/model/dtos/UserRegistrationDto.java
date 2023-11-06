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
public class UserRegistrationDto {


    @NotNull
    @Size(min = 4, message = "First and Last name must be minimum 4 characters")
    private String fullName;
    @NotNull
    @Positive(message = "Age must be positive number")
    private Integer age;
    @Email(message = "This is not a valid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @Size(min = 4, message = "Password must be minimum 4 characters")
    @Size(max = 20, message = "Password must be maximum 20 characters")
    @NotNull
    private String password;

}
