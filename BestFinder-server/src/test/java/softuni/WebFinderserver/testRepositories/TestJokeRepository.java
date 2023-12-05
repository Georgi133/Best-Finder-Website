package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Game;
import softuni.WebFinderserver.model.entities.categories.Joke;

public interface TestJokeRepository extends JpaRepository<Joke, Long> {

    Joke getJokeByJokeName(String animeName);

}
