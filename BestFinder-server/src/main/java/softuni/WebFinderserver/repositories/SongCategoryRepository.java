package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.SongCategory;

@Repository
public interface SongCategoryRepository extends JpaRepository<SongCategory, Long> {


}
