package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.domain.entities.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
