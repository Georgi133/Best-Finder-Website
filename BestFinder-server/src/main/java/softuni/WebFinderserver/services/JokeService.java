package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.repositories.JokeRepository;

@Service
public class JokeService {

    private final JokeRepository jokeRepository;

    public JokeService(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }
    public BaseView createJoke(TorrentUploadDto dto, MultipartFile file) {
        return new BaseView();
    }

}
