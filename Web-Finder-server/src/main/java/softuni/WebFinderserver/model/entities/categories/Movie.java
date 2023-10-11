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
                 LocalDate releaseDate,
                 List<Actor> actors,
                 List<CategoryProjection> categories
    )

    {
        this.resume = resume;
        this.movieName = movieName;
        this.releasedDate = releaseDate;
        this.actors = actors;
        this.setCategories(categories);
    }


    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column
    private String movieName;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;


    @Column(columnDefinition = "DATE")
    private LocalDate releasedDate;

    @Column
    private boolean isLiked;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movies_actors",
            joinColumns = { @JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private List<Actor> actors;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<Like> likes;


//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "movies_categories",
//            joinColumns = { @JoinColumn(name = "movie_id")},
//            inverseJoinColumns = {@JoinColumn(name = "category_id")}
//    )
//    private List<CategoryProjection> categories;


}
