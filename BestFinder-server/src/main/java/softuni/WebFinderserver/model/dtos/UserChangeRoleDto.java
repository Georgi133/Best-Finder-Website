package softuni.WebFinderserver.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangeRoleDto {

    private String email;

    private String changeUserRole;

    private String role;

}
