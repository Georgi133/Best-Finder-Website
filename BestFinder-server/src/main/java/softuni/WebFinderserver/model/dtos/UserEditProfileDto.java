package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditProfileDto {


    private String email;
    @Size(min = 4,message = "First and Last name must be minimum 4 characters")
    @NotNull
    private String fullName;
    @NotNull
    @Positive(message = "Age must be positive number")
    private Integer age;

}
