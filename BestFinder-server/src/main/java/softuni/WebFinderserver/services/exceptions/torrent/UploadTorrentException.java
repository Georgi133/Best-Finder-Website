package softuni.WebFinderserver.services.exceptions.torrent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UploadTorrentException extends TorrentException{
    public UploadTorrentException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
