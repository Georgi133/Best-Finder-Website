package softuni.WebFinderserver.services;

import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.Actor;
import softuni.WebFinderserver.model.entities.Singer;
import softuni.WebFinderserver.repositories.SingerRepository;

import java.util.List;

@Service
public class SingerCleanerService {

    private final SingerRepository singerRepository;

    public SingerCleanerService(SingerRepository singerRepository) {
        this.singerRepository = singerRepository;
    }

    public List<Singer> getSinger (List<String> singers) {
        List<String> singersAsString =
                singers.stream().filter(s -> !s.trim().isBlank() || !s.isEmpty()).toList();

        List<Singer> singersCollection = singersAsString
                .stream()
                .map(actorName -> {
                    if (singerRepository.findFirstByFullName(actorName).isEmpty()) {
                        return new Singer(actorName);
                    } else {
                        return singerRepository.findFirstByFullName(actorName).get();
                    }
                })
                .toList();

        if(singersCollection.isEmpty()) {
            throw new RuntimeException("There should be at least 1 singer");
        }
        return singersCollection;
    }

}
