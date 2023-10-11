package softuni.WebFinderserver.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.domain.entities.categories.BaseEntity;

@Entity
@Table(name = "singers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Singer extends BaseEntity {

    @Column
    private String firstName;

    @Column
    private String lastName;

}
