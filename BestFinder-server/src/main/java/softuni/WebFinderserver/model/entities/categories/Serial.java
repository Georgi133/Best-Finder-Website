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
@Table(name = "serials")
@Getter
@Setter
@NoArgsConstructor

public class Serial extends CataloguesWithCommonCategories {


    public Serial(String serialName,
                  Integer seasons,
                  List<Actor> actors,
                  List<CategoryProjection> categories,
                  String resume) {
        this.serialName = serialName;
        this.seasons = seasons;
        this.actors = actors;
        this.setCategories(categories);
        this.resume = resume;
    }

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(nullable = false)
    private String serialName;

    @Column(nullable = false)
    private Integer seasons;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "serials_actors",
            joinColumns = { @JoinColumn(name = "serial_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private List<Actor> actors;



//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "serials_categories",
//            joinColumns = { @JoinColumn(name = "serial_id")},
//            inverseJoinColumns = {@JoinColumn(name = "category_id")})
//    private List<CategoryProjection> categories;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;


}
