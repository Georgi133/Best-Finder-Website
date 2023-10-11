package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;

@Repository
public interface BaseCatalogueRepository extends JpaRepository<BaseCatalogue,Long> {

}
