package softuni.WebFinderserver.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TorrentUploadDto {

    private String torrent;
    private String torrentName;
    private String torrentResume;
    private String actor1;
    private String actor2;
    private String actor3;
    private String actor4;
    private String actor5;
    private String category1;
    private String category2;
    private String category3;
    private Integer releasedYear;

}
