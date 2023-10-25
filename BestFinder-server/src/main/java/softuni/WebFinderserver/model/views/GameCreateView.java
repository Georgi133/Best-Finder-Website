package softuni.WebFinderserver.model.views;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCreateView extends BaseView{

    private String pictureUrl;
    private String addedDate;
    private String gameName;
    private Integer releasedYear;
    private String resume;


}
