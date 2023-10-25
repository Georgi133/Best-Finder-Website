package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.repositories.SerialRepository;

@Service
public class SerialService {
    private final SerialRepository serialRepository;

    public SerialService(SerialRepository serialRepository) {
        this.serialRepository = serialRepository;
    }

    public BaseView createSerial(TorrentUploadDto dto, MultipartFile file) {
        return new BaseView();
    }

}
