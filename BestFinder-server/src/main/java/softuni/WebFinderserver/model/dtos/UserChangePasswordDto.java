package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {

    private String email;

    @NotNull(message = "Password must be minimum 4 characters")
    @Size(min = 4, message = "Password must be minimum 4 characters")
    private String currentPassword;

    @NotNull(message = "Password must be minimum 4 characters")
    @Size(min = 4, message = "Password must be minimum 4 characters")
    private String newPassword;

    @NotNull(message = "Password must be minimum 4 characters")
    @Size(min = 4, message = "Password must be minimum 4 characters")
    private String confirmPassword;

}
