package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.Singer;

import java.util.Optional;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Long> {

    Optional<Singer> findFirstByFullName(String fullName);

}
