package softuni.WebFinderserver.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;

@Table(name = "songs_categories")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongCategory extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private SongCategoryEnum category;


}
