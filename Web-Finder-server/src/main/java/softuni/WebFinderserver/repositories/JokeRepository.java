package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.domain.entities.categories.Joke;

@Repository
public interface JokeRepository extends JpaRepository<Joke,Long> {


}
