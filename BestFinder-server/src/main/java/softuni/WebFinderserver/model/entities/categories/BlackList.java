package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackList extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String blockedIpAddress;


}
