package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
