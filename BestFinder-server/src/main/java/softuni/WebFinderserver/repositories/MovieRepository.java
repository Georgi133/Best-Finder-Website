package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


}
