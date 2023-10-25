package softuni.WebFinderserver.model.views;

import jakarta.persistence.Column;
import lombok.*;
import softuni.WebFinderserver.model.entities.Singer;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongCreateView extends BaseView{

    private LocalDate addedDate;

    private String pictureUrl;

    private String songName;

    private List<Singer> singers;

}
