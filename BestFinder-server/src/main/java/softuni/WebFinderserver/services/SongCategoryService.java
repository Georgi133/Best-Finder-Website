package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.SongCategory;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;
import softuni.WebFinderserver.repositories.SongCategoryRepository;

import java.util.List;

@Service
public class SongCategoryService {

    private final SongCategoryRepository songCategoryRepository;

    public SongCategoryService(SongCategoryRepository songCategoryRepository) {
        this.songCategoryRepository = songCategoryRepository;
    }

    public void initializeSongCategories () {
        if(songCategoryRepository.count() == 0) {
            SongCategory blues = new SongCategory(SongCategoryEnum.BLUES);
            SongCategory pop = new SongCategory(SongCategoryEnum.POP);
            SongCategory jazz = new SongCategory(SongCategoryEnum.JAZZ);
            SongCategory hipHop = new SongCategory(SongCategoryEnum.HIP_HOP);
            SongCategory classic = new SongCategory(SongCategoryEnum.CLASSICAL);
            SongCategory metal = new SongCategory(SongCategoryEnum.METAL);
            SongCategory rock = new SongCategory(SongCategoryEnum.ROCK);

            songCategoryRepository.saveAll(List.of(blues,
                    pop,jazz,hipHop,classic,metal,rock));
        }
    }

    public SongCategory findCategory(SongCategoryEnum songCategoryEnum) {
        return songCategoryRepository.findFirstByCategory(songCategoryEnum);
    }



}
