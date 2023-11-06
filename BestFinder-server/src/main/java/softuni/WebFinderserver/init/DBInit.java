package softuni.WebFinderserver.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.services.CategoryProjectionService;
import softuni.WebFinderserver.services.EmailService;
import softuni.WebFinderserver.services.SongCategoryService;

import java.util.Locale;
import java.util.UUID;

@Component
public class DBInit implements CommandLineRunner {

    private final CategoryProjectionService categoryProjectionService;
    private final SongCategoryService songCategoryService;
    public DBInit(CategoryProjectionService categoryProjectionService, SongCategoryService songCategoryService) {
        this.categoryProjectionService = categoryProjectionService;
        this.songCategoryService = songCategoryService;

    }

    @Override
    public void run(String... args) throws Exception {


        categoryProjectionService.initializeCategoryProjectionService();
        songCategoryService.initializeSongCategories();
    }



}
