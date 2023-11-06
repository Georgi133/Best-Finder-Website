package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameAnimeUploadDto {

    @NotBlank
    private String torrent;
    @NotNull
    @Size(min = 8, message = "Name must be more or equal to 4 characters")
    private String torrentName;
    @NotNull
    @Size(min = 4, message = "Resume must be more or equal to 4 characters")
    private String torrentResume;
    @NotNull
    private String category1;
    private String category2;
    private String category3;
    @Positive
    @Min(1900)
    @Max(2024)
    @NotNull
    private Integer releasedYear;
    @NotNull
    @Size(min = 8, message = "Trailer must be more or equal to 8 characters")
    private String trailer;

}
