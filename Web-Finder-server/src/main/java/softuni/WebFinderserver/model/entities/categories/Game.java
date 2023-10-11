package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor

public class Game extends CataloguesWithCommonCategories {

    public Game(String gameName,
                Integer releasedYear,
                List<CategoryProjection> categories,
                String resume) {
        this.gameName = gameName;
        this.releasedYear = releasedYear;
        this.setCategories(categories);
        this.resume = resume;
    }

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column
    private String gameName;

    @Column
    private Integer releasedYear;

    @Column
    private boolean isLiked;


//    @ManyToMany
//    @JoinTable(
//            name = "games_categories",
//            joinColumns = { @JoinColumn(name = "game_id")},
//            inverseJoinColumns = {@JoinColumn(name = "category_id")})
//    private List<CategoryProjection> categories;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;



}
