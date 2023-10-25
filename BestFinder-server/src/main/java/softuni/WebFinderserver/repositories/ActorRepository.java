package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Actor;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Long> {
    Optional<Actor> findFirstByFullName(String fullName);
}
