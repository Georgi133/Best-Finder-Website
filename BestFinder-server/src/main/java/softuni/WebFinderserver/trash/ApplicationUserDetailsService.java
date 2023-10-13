package softuni.WebFinderserver.trash;


import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.model.entities.Role;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.repositories.UserRepository;

import java.util.List;

//@Component
//public class ApplicationUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public ApplicationUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .map(this::map)
//                .orElseThrow(() -> new UsernameNotFoundException
//                        ("UserEntity with username " + username + " not found!"));
//    }
//
//    public UserDetails map(UserEntity userEntity) {
//        return new User(
//                userEntity.getUsername(),
//                userEntity.getPassword(),
//                extractAuthorities(userEntity)
//        );
//    }
//
//    public List<GrantedAuthority>extractAuthorities(UserEntity userEntity) {
//        return userEntity.getRoles()
//                .stream().map(this::mapRole)
//                .toList();
//    }
//
//    public GrantedAuthority mapRole(Role role) {
//        return new SimpleGrantedAuthority("ROLE_" + role.getRole().name());
//    }

//
//}
