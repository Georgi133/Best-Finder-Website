package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.BlackList;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    Optional<BlackList> findFirstByBlockedIpAddress(String blockedIpAddress);

    @Query(value = "select * from blacklist as b " +
            "where date_add(b.time_of_ban, interval 24 hour) <= now()",nativeQuery = true)
     List<BlackList> getAllThatHasExpired();

}
