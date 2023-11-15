package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Joke;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface JokeRepository extends JpaRepository<Joke,Long> {

    Optional<Joke> findFirstByJokeName(String jokeName);

    @Query("select j from Joke j order by size(j.likes) desc")
    List<Joke> getAll();


    @Query(value = "select * from jokes as j " +
            "where lower(j.joke_name) regexp lower(:word)", nativeQuery = true)
    Set<Joke> getJokeByCriteria(@Param("word") String word);

    @Query("select j.addedDate from Joke j order by j.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();


}
