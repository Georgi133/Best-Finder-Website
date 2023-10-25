package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;

@Repository
public interface CategoryProjectionRepository extends JpaRepository<CategoryProjection, Long> {

    CategoryProjection findFirstByCategory(CategoryProjectionEnum category);

}
