package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;

import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {

    Optional<Anime> findFirstByAnimeName(String animeName);

}
