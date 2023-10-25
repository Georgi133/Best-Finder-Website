package softuni.WebFinderserver.model.views;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JokeCreateView extends BaseView{

    private String pictureUrl;
    private LocalDate addedDate;
    private String jokeName;
    private String text;

}
