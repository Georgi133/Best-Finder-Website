package softuni.WebFinderserver.model.views;

import lombok.*;
import softuni.WebFinderserver.model.enums.RoleEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginView {

    private Long id;
    private String fullName;
    private Integer age;
    private String email;
    private String token;
    private RoleEnum role;

}
