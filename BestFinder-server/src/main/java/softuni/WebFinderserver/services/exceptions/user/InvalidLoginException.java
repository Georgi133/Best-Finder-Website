package softuni.WebFinderserver.services.exceptions.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidLoginException extends UserException {
    public InvalidLoginException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
