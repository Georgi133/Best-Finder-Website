package softuni.WebFinderserver.web.user.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import softuni.WebFinderserver.model.dtos.ErrorDto;
import softuni.WebFinderserver.services.exceptions.UserDoesNotExistException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {UserDoesNotExistException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException (UserDoesNotExistException ex) {

        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage())
                        .build());
    }

}
