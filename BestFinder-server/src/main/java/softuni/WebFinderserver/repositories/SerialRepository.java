package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.Serial;

@Repository
public interface SerialRepository extends JpaRepository<Serial,Long> {
}
