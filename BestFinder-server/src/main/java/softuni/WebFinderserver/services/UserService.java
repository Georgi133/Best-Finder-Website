package softuni.WebFinderserver.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.dtos.UserRegistrationDto;
import softuni.WebFinderserver.model.entities.User;
import softuni.WebFinderserver.model.views.UserRegisterView;
import softuni.WebFinderserver.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserService(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public UserRegisterView create(UserRegistrationDto userRegistrationDto) {
        if(isUserEmailExist(userRegistrationDto.getEmail()) || isUserUsernameExist(userRegistrationDto.getUsername())) {
            return null;
            // TODO : customError
        }
        User user = modelMapper.map(userRegistrationDto, User.class);
        User userFromBase = userRepository.save(user);

        return modelMapper.map(userFromBase, UserRegisterView.class);
    }

    public boolean isUserEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUserUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


}
