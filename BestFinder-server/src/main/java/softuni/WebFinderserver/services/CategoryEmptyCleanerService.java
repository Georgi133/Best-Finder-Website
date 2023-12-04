package softuni.WebFinderserver.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryEmptyCleanerService {

    private final CategoryProjectionService categoryProjection;

    public CategoryEmptyCleanerService(CategoryProjectionService categoryProjection) {
        this.categoryProjection = categoryProjection;
    }

    public List<CategoryProjection> clearEmptyProperties (List<String> collect) {
        List<String> categoriesAsStrings =
                collect.stream().filter(s -> !s.trim().isBlank() || !s.isEmpty()).toList();

        List<CategoryProjection> categoryCollection = categoriesAsStrings
                .stream()
                .map(categoryAsString ->
                        (categoryProjection.findCategory(CategoryProjectionEnum.valueOf(categoryAsString.toUpperCase()))))
                .toList();

        if(categoryCollection.isEmpty()) {
            throw new TorrentException("There should be at least 1 category", HttpStatus.BAD_REQUEST);
        }
        return categoryCollection;
    }

}
