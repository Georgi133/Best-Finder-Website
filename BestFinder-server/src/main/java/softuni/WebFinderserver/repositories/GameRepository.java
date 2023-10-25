package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Game;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    Optional<Game> findFirstByGameName(String gameName);

}
