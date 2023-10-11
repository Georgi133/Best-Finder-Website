package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
