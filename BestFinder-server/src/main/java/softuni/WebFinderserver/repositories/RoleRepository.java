package softuni.WebFinderserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.WebFinderserver.model.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {


}
