package softuni.WebFinderserver.model.views;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TorrentInfoView {

    private String description;

    private String lastAddedOn;

}
