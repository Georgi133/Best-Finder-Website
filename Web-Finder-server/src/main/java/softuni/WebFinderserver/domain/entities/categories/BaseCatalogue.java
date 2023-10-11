package softuni.WebFinderserver.domain.entities.categories;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "base_catalogues")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseCatalogue extends BaseEntity {


}
