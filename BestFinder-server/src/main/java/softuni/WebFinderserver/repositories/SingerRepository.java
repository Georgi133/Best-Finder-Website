package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Singer;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Long> {

}
