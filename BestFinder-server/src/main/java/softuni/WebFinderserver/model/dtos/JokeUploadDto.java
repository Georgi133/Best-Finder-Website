package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JokeUploadDto {

    @NotBlank
    private String torrent;

    @NotNull
    @Size(min = 8, message = "Name must be more or equal to 4 characters!")
    private String torrentName;

    @NotNull
    @Size(min = 15,message = "Text must be more or equal to 15 characters!")
    private String text;


}
