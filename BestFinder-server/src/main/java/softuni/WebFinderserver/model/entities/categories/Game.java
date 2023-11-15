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



    @Column(nullable = false)
    private String pictureUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(nullable = false, unique = true)
    private String gameName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String trailer;

    @Column(nullable = false)
    private Integer releasedYear;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String resume;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;



}
