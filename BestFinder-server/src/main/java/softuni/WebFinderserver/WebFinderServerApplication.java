package softuni.WebFinderserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebFinderServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFinderServerApplication.class, args);
	}

}
