package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Serial;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SerialRepository extends JpaRepository<Serial,Long> {

    Optional<Serial> findFirstBySerialName(String serialName);

    @Query("select s from Serial s order by size(s.likes) desc")
    List<Serial> getAll();


    @Query("select s from Serial s order by s.seasons desc")
    List<Serial> getAllBySeasonsDesc();

    @Query("select s.addedDate from Serial s order by s.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

}
