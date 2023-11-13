package softuni.WebFinderserver.model.entities.categories;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.CategoryProjection;

import java.util.List;

@Entity
@Table(name = "catalogues")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CataloguesWithCommonCategories extends BaseCatalogue {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "catalogues_common_categories",
            joinColumns = { @JoinColumn(name = "base_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<CategoryProjection> categories;

//    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Comment> comments;

//    @OneToMany(mappedBy = "projectId")
//    private List<Like> likes;

}
