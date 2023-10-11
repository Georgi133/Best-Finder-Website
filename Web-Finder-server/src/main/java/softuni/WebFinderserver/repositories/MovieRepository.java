package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.domain.entities.categories.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
