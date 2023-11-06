package softuni.WebFinderserver.services.exceptions.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UserException extends RuntimeException {

    private final HttpStatus code;

    public UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.code = httpStatus;
    }
}
