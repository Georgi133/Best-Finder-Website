package softuni.WebFinderserver.model.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {

    private String email;

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
