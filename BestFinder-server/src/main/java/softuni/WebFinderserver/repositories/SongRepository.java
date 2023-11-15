package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.Serial;
import softuni.WebFinderserver.model.entities.categories.Song;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {

    Optional<Song> findFirstBySongName(String songName);


    @Query("select s from Song s order by size(s.likes) desc")
    List<Song> getAll();


    @Query("select s from Song s order by s.releasedYear desc")
    List<Song> getAllByYearDesc();

    @Query("select s.addedDate from Song s order by s.addedDate desc limit 1")
    LocalDate getMovieWhichWasLastAdded();

    @Query(value = "select * from songs as s " +
            "join songs_singers as ss on ss.song_id = s.id " +
            "join singers as s2 on s2.id = ss.singers_id " +
            "join songs_songs_categories as ssc on ssc.song_id = s.id " +
            "join songs_categories as sc on sc.id = ssc.category_id " +
            "where lower(sc.category) regexp lower(:word) or lower(s.song_name) regexp lower(:word) or lower(s2.full_name) regexp (:word) " +
            "order by s.released_year desc", nativeQuery = true)
    Set<Song> getSongsByCriteriaAndOrderedByYearDesc(@Param("word") String word);

    @Query(value = "select * from songs as s " +
            "join songs_singers as ss on ss.song_id = s.id " +
            "join singers as s2 on s2.id = ss.singers_id " +
            "join songs_songs_categories as ssc on ssc.song_id = s.id " +
            "join songs_categories as sc on sc.id = ssc.category_id " +
            "where lower(sc.category) regexp lower(:word) or lower(s.song_name) regexp lower(:word) or lower(s2.full_name) regexp (:word) " , nativeQuery = true)
    Set<Song> getSongsByCriteria(@Param("word") String word);


}
