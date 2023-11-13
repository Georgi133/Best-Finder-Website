package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Anime;

public interface TestAnimeRepository extends JpaRepository<Anime,Long> {
}
