package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.CategoryProjection;

@Repository
public interface CategoryProjectionRepository extends JpaRepository<CategoryProjection, Long> {


}
