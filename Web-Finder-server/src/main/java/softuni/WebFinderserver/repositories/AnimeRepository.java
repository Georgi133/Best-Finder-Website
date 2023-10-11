package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.domain.entities.categories.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {


}
