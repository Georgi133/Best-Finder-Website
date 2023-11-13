package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;

public interface TestCategoryProjectionRepository extends JpaRepository<CategoryProjection,Long> {

    CategoryProjection getByCategory(CategoryProjectionEnum category);
}
