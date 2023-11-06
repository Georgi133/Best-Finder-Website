package softuni.WebFinderserver.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SerialUploadDto {

    @NotBlank
    private String torrent;
    @NotNull
    @Size(min = 4, message = "Name must be more or equal to 4 characters")
    private String torrentName;
    @NotNull
    @Size(min = 4, message = "Resume must be more or equal to 4 characters")
    private String torrentResume;
    @Size(min = 4, message = "Actor name must be more or equal to 4 characters")
    private String actor1;
    private String actor2;
    private String actor3;
    private String actor4;
    private String actor5;
    @NotNull
    private String category1;
    private String category2;
    private String category3;
    @Positive
    @NotNull
    @Max(20)
    @Min(1)
    private Integer seasons;

    @NotNull
    @Size(min = 8, message = "Must be more or equal to 8 characters!")
    private String trailer;


}
