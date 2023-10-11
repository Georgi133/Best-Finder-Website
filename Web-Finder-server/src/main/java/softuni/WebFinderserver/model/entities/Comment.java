package softuni.WebFinderserver.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    public Comment(String text, CataloguesWithCommonCategories category, User createdBy) {
        this.catalogue = category;
        this.createdBy = createdBy;
        this.text = text;
    }

    private String text;

    @ManyToOne
    private BaseCatalogue catalogue;

    @ManyToOne
    private User createdBy;


}
