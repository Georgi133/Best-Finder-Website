package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.repositories.SongRepository;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public BaseView createSong(TorrentUploadDto dto, MultipartFile file) {
        return  new BaseView();
    }


}
