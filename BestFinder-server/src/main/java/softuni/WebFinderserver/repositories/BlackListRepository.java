package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.categories.BlackList;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    Optional<BlackList> findFirstByBlockedIpAddress(String blockedIpAddress);

}
