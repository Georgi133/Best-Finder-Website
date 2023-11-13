package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Song;

public interface TestSongRepository extends JpaRepository<Song,Long> {
}
