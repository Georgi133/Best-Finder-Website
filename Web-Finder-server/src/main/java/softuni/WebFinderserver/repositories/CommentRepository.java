package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}
