package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.domain.entities.categories.BaseCatalogue;

public interface BaseCatalogueRepository extends JpaRepository<BaseCatalogue,Long> {



}
