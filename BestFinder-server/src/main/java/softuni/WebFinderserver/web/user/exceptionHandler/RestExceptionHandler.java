package softuni.WebFinderserver.web.user.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import softuni.WebFinderserver.model.dtos.ErrorDto;
import softuni.WebFinderserver.services.exceptions.user.*;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({InvalidLoginException.class, InvalidRegisterException.class, InvalidPasswordException.class, UserException.class, UnAuthorizedException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException (UserException ex) {

        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage())
                        .build());
    }

}
