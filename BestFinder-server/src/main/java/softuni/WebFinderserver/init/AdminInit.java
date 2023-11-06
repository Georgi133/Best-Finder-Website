package softuni.WebFinderserver.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.enums.RoleEnum;
import softuni.WebFinderserver.repositories.UserRepository;

@Component
public class AdminInit implements CommandLineRunner {

    @Value("${best-finder.admin}")
    private String email;

    @Value("${best-finder.defaultpass}")
    private String password;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        adminInit(this.email, this.password);
    }

    private void adminInit(String email , String password) {
        if(!userRepository.findByEmail(email).isPresent()) {
            UserEntity admin = UserEntity.builder()
                    .pass(passwordEncoder.encode(password))
                    .age(999)
                    .role(RoleEnum.ADMIN)
                    .email(email)
                    .ipAddress("unknown")
                    .fullName("Admin Adminov")
                    .build();

            userRepository.save(admin);
        }

    }

}
