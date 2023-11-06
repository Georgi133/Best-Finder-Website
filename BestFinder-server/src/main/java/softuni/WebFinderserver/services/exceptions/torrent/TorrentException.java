package softuni.WebFinderserver.services.exceptions.torrent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TorrentException extends RuntimeException {

    private final HttpStatus code;

    public TorrentException(String message, HttpStatus httpStatus) {
        super(message);
        this.code = httpStatus;
    }


}