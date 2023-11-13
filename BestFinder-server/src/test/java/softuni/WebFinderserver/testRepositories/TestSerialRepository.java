package softuni.WebFinderserver.testRepositories;


import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.categories.Serial;

public interface TestSerialRepository extends JpaRepository<Serial,Long> {


}
