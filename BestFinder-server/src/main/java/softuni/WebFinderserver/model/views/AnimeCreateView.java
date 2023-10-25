package softuni.WebFinderserver.model.views;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Builder
@Service
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnimeCreateView extends BaseView {

    private String pictureUrl;

    private String addedDate;

    private String animeName;

    private Integer releasedYear;

    private String resume;


}
