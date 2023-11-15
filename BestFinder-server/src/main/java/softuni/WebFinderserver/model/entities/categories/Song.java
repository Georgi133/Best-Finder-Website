package softuni.WebFinderserver.model.entities.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.Singer;
import softuni.WebFinderserver.model.entities.SongCategory;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Song extends BaseCatalogue {
    public Song(String songName,
                List<SongCategory> categories) {
        this.songName = songName;
        this.categories = categories;
    }

    @Column(nullable = false)
    private String pictureUrl;

    @Column(nullable = false, unique = true)
    private String songName;


    @Column(nullable = false)
    private Integer releasedYear;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String songVideo;

    @ManyToMany
    @JoinTable(
            name = "songs_songs_categories",
            joinColumns = { @JoinColumn(name = "song_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<SongCategory> categories;

    @OneToMany(mappedBy = "catalogue",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Singer> singers;

    @OneToMany(mappedBy = "project")
    private List<Like> likes;

    @Column(columnDefinition = "DATE")
    private LocalDate addedDate;

}
