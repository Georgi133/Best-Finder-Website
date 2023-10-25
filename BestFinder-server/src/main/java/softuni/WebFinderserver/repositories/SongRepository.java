package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Song;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {

}
