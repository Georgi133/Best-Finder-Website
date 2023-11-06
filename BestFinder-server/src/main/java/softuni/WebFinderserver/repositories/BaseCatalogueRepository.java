package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.BaseCatalogue;

import java.time.LocalDate;

@Repository
public interface BaseCatalogueRepository extends JpaRepository<BaseCatalogue,Long> {

}
