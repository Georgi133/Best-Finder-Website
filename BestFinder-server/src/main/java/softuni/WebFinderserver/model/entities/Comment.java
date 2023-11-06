package softuni.WebFinderserver.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;
import softuni.WebFinderserver.model.entities.categories.CataloguesWithCommonCategories;

@Table(name = "comments")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseEntity {

    public Comment(String text, BaseCatalogue category, UserEntity createdBy) {
        this.catalogue = category;
        this.createdBy = createdBy;
        this.text = text;
    }

    @Column(nullable = false)
    private String text;

    @ManyToOne
    private BaseCatalogue catalogue;

    @ManyToOne
    private UserEntity createdBy;


}
