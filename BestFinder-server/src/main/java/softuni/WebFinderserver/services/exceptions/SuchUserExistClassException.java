package softuni.WebFinderserver.services.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SuchUserExistClassException extends RuntimeException{

    private final HttpStatus code;

    public SuchUserExistClassException(String message, HttpStatus httpStatus) {
        super(message);
        this.code = httpStatus;
    }


}
