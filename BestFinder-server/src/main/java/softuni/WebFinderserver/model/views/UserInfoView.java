package softuni.WebFinderserver.model.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import softuni.WebFinderserver.model.enums.RoleEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoView {

    private Long id;
    private String fullName;
    private Integer age;
    private String email;
    private RoleEnum role;

}
