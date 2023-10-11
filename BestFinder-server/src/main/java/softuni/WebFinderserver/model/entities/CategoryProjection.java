package softuni.WebFinderserver.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;

@Table(name = "categories_projections")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryProjection extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private CategoryProjectionEnum category;

}
