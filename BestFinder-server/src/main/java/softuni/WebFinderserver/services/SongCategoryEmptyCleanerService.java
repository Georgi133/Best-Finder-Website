package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.SongCategory;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.model.enums.SongCategoryEnum;

import java.util.List;

@Service
public class SongCategoryEmptyCleanerService {

    private final SongCategoryService songCategoryService;

    public SongCategoryEmptyCleanerService(SongCategoryService songCategoryService) {
        this.songCategoryService = songCategoryService;
    }

    public List<SongCategory> clearEmptyProperties (List<String> collect) {
        List<String> categoriesAsStrings =
                collect.stream().filter(s -> !s.trim().isBlank() || !s.isEmpty()).toList();

        List<SongCategory> categoryCollection = categoriesAsStrings
                .stream()
                .map(categoryAsString ->
                        (songCategoryService.findCategory(SongCategoryEnum.valueOf(categoryAsString.toUpperCase()))))
                .toList();

        if(categoryCollection.isEmpty()) {
            throw new RuntimeException("There should be at least 1 category");
        }
        return categoryCollection;
    }
}
