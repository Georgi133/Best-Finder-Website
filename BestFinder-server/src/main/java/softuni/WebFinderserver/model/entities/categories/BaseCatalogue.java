package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "base_catalogues")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseCatalogue extends BaseEntity {


}
