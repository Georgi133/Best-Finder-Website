package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.UserEntity;

public interface TestUserRepository extends JpaRepository<UserEntity, Long> {
}
