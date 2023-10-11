package softuni.WebFinderserver.domain.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.domain.entities.Comment;
import softuni.WebFinderserver.domain.entities.Like;

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

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

    @Column
    private String jokeName;

    @Column
    private boolean isLiked;

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;


    @OneToMany(mappedBy = "project")
    private List<Like> likes;


}
