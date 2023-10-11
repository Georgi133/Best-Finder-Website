package softuni.WebFinderserver.model.entities;

import jakarta.persistence.*;
import lombok.*;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;

@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like extends BaseEntity {

    @JoinColumn(name = "project_id")
    @ManyToOne
    private BaseCatalogue project;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

}
