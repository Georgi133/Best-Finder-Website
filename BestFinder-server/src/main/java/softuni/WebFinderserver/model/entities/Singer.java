package softuni.WebFinderserver.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;

@Entity
@Table(name = "singers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Singer extends BaseEntity {

    @Column(unique = true)
    private String fullName;


}
