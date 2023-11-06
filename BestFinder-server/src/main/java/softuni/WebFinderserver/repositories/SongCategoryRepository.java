package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.SongCategory;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;

@Repository
public interface SongCategoryRepository extends JpaRepository<SongCategory, Long> {

    SongCategory findFirstByCategory(SongCategoryEnum category);

}
