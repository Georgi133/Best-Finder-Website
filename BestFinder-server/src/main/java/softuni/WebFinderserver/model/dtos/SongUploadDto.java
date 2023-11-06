package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongUploadDto {

    @NotBlank
    private String torrent;
    @NotNull
    @Size(min = 4, message = "Name must be more or equal to 4 characters")
    private String torrentName;

    @Size(min = 4, message = "Singer must be more or equal to 4 characters")
    @NotNull
    private String singer1;
    private String singer2;
    private String singer3;
    private String singer4;
    private String singer5;
    @NotNull
    private String category1;
    private String category2;
    private String category3;
    @NotNull
    @Min(1900)
    @Max(2024)
    private Integer releasedYear;

    @NotNull
    @Size(min = 8, message = "Video link must be more or equal to 8 characters")
    private String songVideo;
    

}
