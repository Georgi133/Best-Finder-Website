package softuni.WebFinderserver.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.services.CategoryProjectionService;
import softuni.WebFinderserver.services.SongCategoryService;
import softuni.WebFinderserver.services.businessServicesInt.BlackListService;

@Component
public class DBInit implements CommandLineRunner {

    private final CategoryProjectionService categoryProjectionService;
    private final SongCategoryService songCategoryService;

    private final BlackListService blackListService;
    public DBInit(CategoryProjectionService categoryProjectionService, SongCategoryService songCategoryService, BlackListService blackListService) {
        this.categoryProjectionService = categoryProjectionService;
        this.songCategoryService = songCategoryService;
        this.blackListService = blackListService;
    }

    @Override
    public void run(String... args)  {

        System.out.println(blackListService.removeIpAddressFromBlackList("85.91.145.148"));
        categoryProjectionService.initializeCategoryProjectionService();
        songCategoryService.initializeSongCategories();
    }



}
