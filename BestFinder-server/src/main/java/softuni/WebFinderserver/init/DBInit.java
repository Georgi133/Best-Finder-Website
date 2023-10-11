package softuni.WebFinderserver.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.services.CategoryProjectionService;
import softuni.WebFinderserver.services.RoleService;
import softuni.WebFinderserver.services.SongCategoryService;

@Component
public class DBInit implements CommandLineRunner {

    private final RoleService roleService;
    private final CategoryProjectionService categoryProjectionService;
    private final SongCategoryService songCategoryService;

    public DBInit(RoleService roleService, CategoryProjectionService categoryProjectionService, SongCategoryService songCategoryService) {
        this.roleService = roleService;
        this.categoryProjectionService = categoryProjectionService;
        this.songCategoryService = songCategoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.initializeRoles();
        categoryProjectionService.initializeCategoryProjectionService();
        songCategoryService.initializeSongCategories();

    }



}
