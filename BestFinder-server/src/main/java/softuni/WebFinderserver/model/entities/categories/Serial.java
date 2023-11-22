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
        this.torrentName = serialName;
        this.seasons = seasons;
        this.actors = actors;
        this.setCategories(categories);
        this.torrentResume = resume;
    }


    @Column(nullable = false)
    private String pictureUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(name = "serial_name",nullable = false , unique = true)
    private String torrentName;

    @Column(nullable = false)
    private Integer seasons;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String trailer;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "serials_actors",
            joinColumns = { @JoinColumn(name = "serial_id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private List<Actor> actors;

    @Column(name = "resume",columnDefinition = "TEXT")
    private String torrentResume;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "project")
    private List<Like> likes;


}
