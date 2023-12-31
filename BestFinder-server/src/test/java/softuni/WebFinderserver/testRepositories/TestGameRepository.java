package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Game;

public interface TestGameRepository extends JpaRepository<Game,Long> {
    Game getGameByTorrentName(String animeName);
}
