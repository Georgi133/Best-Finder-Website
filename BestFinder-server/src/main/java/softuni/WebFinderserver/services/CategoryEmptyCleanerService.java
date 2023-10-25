package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;

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
                collect.stream().filter(s -> !s.isBlank() || !s.isEmpty()).toList();

        return categoriesAsStrings
                .stream()
                .map(categoryAsString ->
                        (categoryProjection.findCategory(CategoryProjectionEnum.valueOf(categoryAsString.toUpperCase()))))
                .collect(Collectors.toList());

    }

}
