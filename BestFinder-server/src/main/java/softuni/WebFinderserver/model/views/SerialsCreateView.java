package softuni.WebFinderserver.model.views;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialsCreateView extends BaseView{

    private String pictureUrl;
    private LocalDate addedDate;
    private String serialName;
    private Integer seasons;
    private String resume;


}
