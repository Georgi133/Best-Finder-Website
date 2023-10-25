package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.views.AnimeCreateView;
import softuni.WebFinderserver.repositories.AnimeRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;

    public AnimeService(AnimeRepository animeRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner) {
        this.animeRepository = animeRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
    }

    public AnimeCreateView createAnime (TorrentUploadDto dto, MultipartFile file) throws IOException {
        Optional<Anime> anime = animeRepository.findFirstByAnimeName(dto.getTorrentName());
        if(anime.isPresent()) {
            throw new RuntimeException("Such anime already exist");
        }
        Anime mappedAnimeToBeSaved = mapToAnime(dto, file);
        Anime savedAnime = animeRepository.save(mappedAnimeToBeSaved);

        return mapToView(savedAnime);
    }
    private AnimeCreateView mapToView(Anime savedAnime) {
        AnimeCreateView build = AnimeCreateView.builder()
                .resume(savedAnime.getResume())
                .animeName(savedAnime.getAnimeName())
                .releasedYear(savedAnime.getReleasedYear())
                .addedDate(savedAnime.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedAnime.getPictureUrl())
                .build();
        build.setId(savedAnime.getId());

        return build;
    }
    private Anime mapToAnime(TorrentUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Anime anime1 = new Anime();
        anime1.setAnimeName(dto.getTorrentName());
        anime1.setLikes(new ArrayList<>());
        anime1.setComments(new ArrayList<>());
        anime1.setResume(dto.getTorrentResume());
        anime1.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        anime1.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        anime1.setAddedDate(LocalDate.now());
        anime1.setReleasedYear(dto.getReleasedYear());

        return anime1;
    }



}
