package softuni.WebFinderserver.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.domain.entities.categories.BaseEntity;
import softuni.WebFinderserver.domain.enums.SongCategoryEnum;

@Table(name = "songs_categories")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongCategory extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private SongCategoryEnum category;


}
