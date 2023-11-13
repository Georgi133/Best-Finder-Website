package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query(value = "select * from games as g " +
            "join catalogues_common_categories as cmc " +
            "on cmc.base_id = g.id " +
            "join categories_projections as cp " +
            "on cp.id = cmc.category_id " +
            "where lower(g.game_name) regexp lower(:word) or lower(cp.category) regexp lower(:word) " +
            "order by g.released_year desc",nativeQuery = true)
    List<Game> getAllByCriteriaOrderedByYearDesc(@Param("word") String word);

    @Query(value = "select * from games as g " +
            "join catalogues_common_categories as cmc " +
            "on cmc.base_id = g.id " +
            "join categories_projections as cp " +
            "on cp.id = cmc.category_id " +
            "where lower(g.game_name) regexp lower(:word) or lower(cp.category) regexp lower(:word)" ,nativeQuery = true)
    List<Game> getAllByCriteria(@Param("word") String word);


    @Query("select g from Game g order by g.releasedYear desc")
    List<Game> getAllByYearDesc();

    @Query("select g.addedDate from Game g order by g.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

}
