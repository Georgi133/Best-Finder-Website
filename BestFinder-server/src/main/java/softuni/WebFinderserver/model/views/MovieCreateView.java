package softuni.WebFinderserver.model.views;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MovieCreateView extends BaseView{

    private String pictureUrl;
    private String addedDate;
    private String movieName;
    private String resume;
    private Integer releasedYear;
    private List<String> actors;

}
