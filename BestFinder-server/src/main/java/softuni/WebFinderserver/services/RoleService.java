package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.Role;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void initializeRoles () {
        if(roleRepository.count() == 0) {
            Role user = new Role(RoleEnum.USER);
            Role admin = new Role(RoleEnum.ADMIN);
            roleRepository.saveAll(List.of(user,admin));
        }
    }
}
