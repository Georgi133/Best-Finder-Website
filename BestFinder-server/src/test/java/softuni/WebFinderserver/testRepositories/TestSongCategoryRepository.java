package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.SongCategory;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;

public interface TestSongCategoryRepository extends JpaRepository<SongCategory,Long> {

    SongCategory getByCategory(SongCategoryEnum category);
}
