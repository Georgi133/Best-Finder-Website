package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.entities.categories.Serial;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SerialRepository extends JpaRepository<Serial,Long> {

    Optional<Serial> findFirstBySerialName(String serialName);

    @Query("select s from Serial s order by size(s.likes) desc")
    List<Serial> getAll();


    @Query("select s from Serial s order by s.seasons desc")
    List<Serial> getAllBySeasonsDesc();

    @Query("select s.addedDate from Serial s order by s.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

    @Query(value = "select * from serials as s " +
            "join serials_actors as sa on sa.serial_id = s.id " +
            "join actors as a on sa.actor_id = a.id " +
            "join catalogues_common_categories as ccc on ccc.base_id = s.id " +
            "join categories_projections as cp on cp.id = ccc.category_id " +
            "where lower(cp.category) regexp lower(:word) or lower(s.serial_name) regexp lower(:word) or lower(a.full_name) regexp (:word)",nativeQuery = true)
    Set<Serial> getSerialsByCriteria(@Param("word") String word);



}
