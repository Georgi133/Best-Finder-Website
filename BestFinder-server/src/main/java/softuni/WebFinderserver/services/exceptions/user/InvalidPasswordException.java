package softuni.WebFinderserver.services.exceptions.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidPasswordException extends UserException {
    public InvalidPasswordException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
