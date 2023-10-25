package softuni.WebFinderserver.model.dtos;

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
    private String newEmail;
    private String fullName;
    private Integer age;

}
