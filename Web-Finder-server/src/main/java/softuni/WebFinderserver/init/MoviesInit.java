package softuni.WebFinderserver.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.repositories.*;

import java.time.LocalDate;
import java.util.List;

@Component
public class MoviesInit implements CommandLineRunner {
    private final BaseCatalogueRepository baseCatalogueRepository;

    public MoviesInit(BaseCatalogueRepository baseCatalogueRepository) {
        this.baseCatalogueRepository = baseCatalogueRepository;
    }

//    private final AnimeRepository animeRepository;
//    private final JokeRepository jokeRepository;
//    private final CategoryAllRepository categoryAllRepository;
//    private final CommentRepository commentRepository;
//    private final UserRepository userRepository;
//    private final LikeRepository likeRepository;
//    private final MovieRepository movieRepository;
//
//    public MoviesInit(AnimeRepository animeRepository, JokeRepository jokeRepository, CategoryAllRepository categoryAllRepository, CommentRepository commentRepository, UserRepository userRepository, LikeRepository likeRepository, MovieRepository movieRepository) {
//        this.animeRepository = animeRepository;
//        this.jokeRepository = jokeRepository;
//        this.categoryAllRepository = categoryAllRepository;
//        this.commentRepository = commentRepository;
//        this.userRepository = userRepository;
//        this.likeRepository = likeRepository;
//        this.movieRepository = movieRepository;
//    }

    @Override
    public void run(String... args) throws Exception {

        Movie movie = new Movie("Gladiators", "Sword battles", LocalDate.now(),
                List.of(new Actor("Brad", "Pitt")),
                List.of(new CategoryProjection(CategoryProjectionEnum.ACTION)));

        baseCatalogueRepository.saveAndFlush(movie);

    }


}
