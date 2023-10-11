package softuni.WebFinderserver.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;

@Table(name = "actors")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Actor extends BaseEntity {

    private String firstName;
    private String fullName;


}
