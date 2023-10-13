package softuni.WebFinderserver.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class MessageLogin {


    @GetMapping("/message222")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello");
    }


}
