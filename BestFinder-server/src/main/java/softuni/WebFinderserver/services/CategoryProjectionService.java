package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;

import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.repositories.CategoryProjectionRepository;

import java.util.List;

@Service
public class CategoryProjectionService {

    private final CategoryProjectionRepository categoryProjectionRepository;

    public CategoryProjectionService(CategoryProjectionRepository categoryProjectionRepository) {
        this.categoryProjectionRepository = categoryProjectionRepository;
    }

    public void initializeCategoryProjectionService () {
        if(categoryProjectionRepository.count() == 0) {
            CategoryProjection action = new CategoryProjection(CategoryProjectionEnum.ACTION);
            CategoryProjection adventure = new CategoryProjection(CategoryProjectionEnum.ADVENTURE);
            CategoryProjection comedy = new CategoryProjection(CategoryProjectionEnum.COMEDY);
            CategoryProjection crime = new CategoryProjection(CategoryProjectionEnum.CRIME);
            CategoryProjection drama = new CategoryProjection(CategoryProjectionEnum.DRAMA);
            CategoryProjection fantasy = new CategoryProjection(CategoryProjectionEnum.FANTASY);
            CategoryProjection horror = new CategoryProjection(CategoryProjectionEnum.HORROR);
            CategoryProjection romance = new CategoryProjection(CategoryProjectionEnum.ROMANCE);

            categoryProjectionRepository.saveAll(List.of(
                    action,adventure,comedy,
                    crime,drama,fantasy,horror,romance));

        }
    }

    public CategoryProjection findCategory(CategoryProjectionEnum categoryProjectionEnum) {
        return categoryProjectionRepository.findFirstByCategory(categoryProjectionEnum);
    }

}
