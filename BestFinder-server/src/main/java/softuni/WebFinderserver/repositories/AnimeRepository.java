package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.categories.Anime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {

    Optional<Anime> findFirstByAnimeName(String animeName);

    @Query("select a from Anime a order by size(a.likes) desc")
    List<Anime> getAll();

    @Query(value = "select * from animes as a " +
            "join catalogues_common_categories as cmc " +
            "on cmc.base_id = a.id " +
            "join categories_projections as cp " +
            "on cp.id = cmc.category_id " +
            "where lower(a.anime_name) regexp lower(:word) or lower(cp.category) regexp lower(:word) " +
            "order by a.released_year desc", nativeQuery = true)
    List<Anime> getAllByCriteriaOrderedByYearDesc(@Param("word") String word);

    @Query(value = "select * from animes as a " +
            "join catalogues_common_categories as cmc " +
            "on cmc.base_id = a.id " +
            "join categories_projections as cp " +
            "on cp.id = cmc.category_id " +
            "where lower(a.anime_name) regexp lower(:word) or lower(cp.category) regexp lower(:word)", nativeQuery = true)
    List<Anime> getAllByCriteria(@Param("word") String word);


    @Query("select a from Anime a order by a.releasedYear desc")
    List<Anime> getAllByYearDesc();

    @Query("select a.addedDate from Anime a order by a.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();


}
