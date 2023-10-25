package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "animes")
@Getter
@Setter
@NoArgsConstructor
public class Anime extends CataloguesWithCommonCategories {

    public Anime(String animeName,
                 Integer releasedYear,
                 List<CategoryProjection> categories,
                 String resume) {
//        this.setComments(comments);
        this.animeName = animeName;
        this.releasedYear = releasedYear;
        this.setCategories(categories);
        this.resume = resume;
    }

    @Column(nullable = false)
    private String pictureUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(nullable = false, unique = true)
    private String animeName;

    @Column
    private Integer releasedYear;


//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "animes_categories",
//            joinColumns = { @JoinColumn(name = "anime_id")},
//            inverseJoinColumns = {@JoinColumn(name = "category_id")})
//    private List<CategoryProjection> categories;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String resume;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;

}
