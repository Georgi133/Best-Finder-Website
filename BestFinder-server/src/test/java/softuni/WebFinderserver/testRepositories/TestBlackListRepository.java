package softuni.WebFinderserver.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.WebFinderserver.model.entities.BlackList;

public interface TestBlackListRepository extends JpaRepository<BlackList,Long> {


}
