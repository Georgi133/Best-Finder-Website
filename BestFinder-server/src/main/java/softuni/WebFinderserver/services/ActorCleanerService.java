package softuni.WebFinderserver.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.CategoryProjection;
import softuni.WebFinderserver.model.entities.categories.Movie;
import softuni.WebFinderserver.model.enums.CategoryProjectionEnum;
import softuni.WebFinderserver.repositories.ActorRepository;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorCleanerService {

    private final ActorRepository actorRepository;

    public ActorCleanerService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> getActor (List<String> actors) {
        List<String> actorsAsString =
                actors.stream().filter(s -> !s.trim().isBlank() || !s.isEmpty()).toList();

        List<Actor> actorsCollection = actorsAsString
                .stream()
                .map(actorName -> {
                    if (actorRepository.findFirstByFullName(actorName).isEmpty()) {
                        return new Actor(actorName);
                    } else {
                        return actorRepository.findFirstByFullName(actorName).get();
                    }
                })
                .toList();

        if(actorsCollection.isEmpty()) {
            throw new TorrentException("There should be at least 1 actor", HttpStatus.BAD_REQUEST);
        }
        return actorsCollection;
    }

}
