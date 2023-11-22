package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie extends CataloguesWithCommonCategories {

    public Movie(String movieName,
                 String resume,
                 Integer releasedYear,
                 List<Actor> actors,
                 List<CategoryProjection> categories
    )

    {
        this.torrentResume = resume;
        this.torrentName = movieName;
        this.releasedYear = releasedYear;
        this.actors = actors;
        this.setCategories(categories);
    }

    @Column(nullable = false)
    private String pictureUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String trailer;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(name = "movie_name", nullable = false, unique = true)
    private String torrentName;

    @Column(name = "resume",columnDefinition = "TEXT", nullable = false)
    private String torrentResume;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Column(nullable = false)
    private Integer releasedYear;


    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movies_actors",
            joinColumns = { @JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private List<Actor> actors;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    private List<Like> likes;


}
