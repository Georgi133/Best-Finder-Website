package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Movie;

public interface TestMovieRepository extends JpaRepository<Movie, Long> {
}
