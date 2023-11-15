package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "jokes")
@Getter
@Setter
@NoArgsConstructor
public class Joke extends BaseCatalogue {

    public Joke(String jokeName, String text
//
    ) {
        this.text = text;
        this.jokeName = jokeName;
    }


    @Column(nullable = false)
    private String pictureUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column(nullable = false, unique = true)
    private String jokeName;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;


    @OneToMany(mappedBy = "project")
    private List<Like> likes;


}
