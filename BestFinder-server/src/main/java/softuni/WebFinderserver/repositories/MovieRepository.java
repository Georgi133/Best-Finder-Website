package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findFirstByMovieName(String movieName);

    @Query("select m from Movie m order by size(m.likes) desc")
    List<Movie> getAll();


    @Query("select m from Movie m order by m.releasedYear desc")
    List<Movie> getAllByYearDesc();

    @Query("select m.addedDate from Movie m order by m.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();
    @Query(value = "select * from movies as m " +
            "join movies_actors as ma on ma.movie_id = m.id " +
            "join actors as a on ma.actor_id = a.id " +
            "join catalogues_common_categories as ccc on ccc.base_id = m.id " +
            "join categories_projections as cp on cp.id = ccc.category_id " +
            "where lower(cp.category) regexp lower(:word) or lower(m.movie_name) regexp lower(:word) or lower(a.full_name) regexp (:word) " +
            "order by m.released_year desc ",nativeQuery = true)
    List<Movie> getMoviesByCriteriaOrderedByYearDesc(@Param("word") String word);


    @Query(value = "select * from movies as m " +
            "join movies_actors as ma on ma.movie_id = m.id " +
            "join actors as a on ma.actor_id = a.id " +
            "join catalogues_common_categories as ccc on ccc.base_id = m.id " +
            "join categories_projections as cp on cp.id = ccc.category_id " +
            "where lower(cp.category) regexp lower(:word) or lower(m.movie_name) regexp lower(:word) or lower(a.full_name) regexp (:word) ",nativeQuery = true)
    List<Movie> getMoviesByCriteriaOrderedByLikesDesc(@Param("word") String word);




}
