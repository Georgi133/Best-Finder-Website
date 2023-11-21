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
        this.torrentName = gameName;
        this.releasedYear = releasedYear;
        this.setCategories(categories);
        this.torrentResume = resume;
    }



    @Column(nullable = false)
    private String pictureUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(name = "game_name",nullable = false, unique = true)
    private String torrentName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String trailer;

    @Column(nullable = false)
    private Integer releasedYear;

    @Column(name = "resume",columnDefinition = "TEXT", nullable = false)
    private String torrentResume;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;



}
