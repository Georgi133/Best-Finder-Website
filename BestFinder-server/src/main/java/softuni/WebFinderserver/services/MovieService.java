package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.WebFinderserver.model.dtos.TorrentUploadDto;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.MovieCreateView;
import softuni.WebFinderserver.repositories.MovieRepository;
import softuni.WebFinderserver.util.CloudUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final CloudUtil cloudUtil;
    private final CategoryEmptyCleanerService categoryCleaner;
    private final ActorCleanerService actorCleanerService;

    public MovieService(MovieRepository movieRepository, CloudUtil cloudUtil, CategoryEmptyCleanerService categoryCleaner, ActorCleanerService actorCleanerService) {
        this.movieRepository = movieRepository;
        this.cloudUtil = cloudUtil;
        this.categoryCleaner = categoryCleaner;
        this.actorCleanerService = actorCleanerService;
    }

    public BaseView createMovie (TorrentUploadDto dto, MultipartFile file) throws IOException {
        Optional<Movie> movie = movieRepository.findFirstByMovieName(dto.getTorrentName());

        if(movie.isPresent()) {
            throw new RuntimeException("Such movie already exist");
        }
        Movie mappedMovieBeforeSaving = mapToMovie(dto, file);
        Movie savedMovie = movieRepository.save(mappedMovieBeforeSaving);

        return mapToView(savedMovie);
    }
    private BaseView mapToView(Movie savedMovie) {
        MovieCreateView build = MovieCreateView.builder()
                .resume(savedMovie.getResume())
                .releasedYear(savedMovie.getReleasedYear())
                .addedDate(savedMovie.getAddedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pictureUrl(savedMovie.getPictureUrl())
                .actors(savedMovie.getActors().stream().map(Actor::getFullName).collect(Collectors.toList()))
                .build();
        build.setId(savedMovie.getId());

        return build;
    }
    private Movie mapToMovie(TorrentUploadDto dto, MultipartFile file) throws IOException {
        String urlUploaded = cloudUtil.upload(file);

        Movie movie = new Movie();
        movie.setMovieName(dto.getTorrentName());
        movie.setLikes(new ArrayList<>());
        movie.setComments(new ArrayList<>());
        movie.setResume(dto.getTorrentResume());
        movie.setActors(actorCleanerService
                .getActor(List.of(dto.getActor1(),dto.getActor2(),dto.getActor3(),dto.getActor4(),dto.getActor5())));
        movie.setCategories(categoryCleaner
                .clearEmptyProperties(List.of(dto.getCategory1(), dto.getCategory2(), dto.getCategory3())));
        movie.setPictureUrl(cloudUtil.takeUrl(urlUploaded));
        movie.setAddedDate(LocalDate.now());
        movie.setReleasedYear(dto.getReleasedYear());

        return movie;
    }


}
