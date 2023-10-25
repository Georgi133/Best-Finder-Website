package softuni.WebFinderserver.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.services.CategoryProjectionService;
import softuni.WebFinderserver.services.SongCategoryService;
import softuni.WebFinderserver.services.UserService;

@Component
public class DBInit implements CommandLineRunner {

    private final CategoryProjectionService categoryProjectionService;
    private final SongCategoryService songCategoryService;

    public DBInit(CategoryProjectionService categoryProjectionService, SongCategoryService songCategoryService, UserService userService) {
        this.categoryProjectionService = categoryProjectionService;
        this.songCategoryService = songCategoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryProjectionService.initializeCategoryProjectionService();
        songCategoryService.initializeSongCategories();
    }



}
