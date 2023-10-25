package softuni.WebFinderserver.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindByEmailDto {

    private String email;

    private String currentUserRole;

}
