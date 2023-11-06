package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Song;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {

    Optional<Song> findFirstBySongName(String songName);


    @Query("select s from Song s order by size(s.likes) desc")
    List<Song> getAll();


    @Query("select s from Song s order by s.releasedYear desc")
    List<Song> getAllByYearDesc();

    @Query("select s.addedDate from Song s order by s.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

}
