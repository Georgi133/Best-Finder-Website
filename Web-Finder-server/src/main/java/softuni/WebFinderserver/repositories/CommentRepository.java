package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.domain.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}
