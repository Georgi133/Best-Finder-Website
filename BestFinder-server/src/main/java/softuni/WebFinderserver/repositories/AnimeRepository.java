package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


    @Query("select a from Anime a order by a.releasedYear desc")
    List<Anime> getAllByYearDesc();

    @Query("select a.addedDate from Anime a order by a.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();


}
