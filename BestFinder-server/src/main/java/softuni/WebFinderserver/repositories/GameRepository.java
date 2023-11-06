package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    Optional<Game> findFirstByGameName(String gameName);

    @Query("select g from Game g order by size(g.likes) desc")
    List<Game> getAll();


    @Query("select g from Game g order by g.releasedYear desc")
    List<Game> getAllByYearDesc();

    @Query("select g.addedDate from Game g order by g.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

}
