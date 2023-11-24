package softuni.WebFinderserver.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.Like;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.entities.categories.Anime;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;
import softuni.WebFinderserver.repositories.LikeRepository;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }


    public Like saveLike(Like like) {
        return likeRepository.save(like);
    }

    public void removeLike(BaseEntity baseCategory, UserEntity userByEmail) {
        Like like = likeRepository.findFirstByProject_IdAndUser_Id(baseCategory.getId(), userByEmail.getId())
                .orElseThrow(() -> new TorrentException("No such torrent or user when removing like", HttpStatus.BAD_REQUEST));
        likeRepository.delete(like);
    }


    public List<Like> getLikesOfTorrent(Long id) {
       return likeRepository.findAllByProject_Id(id);
    }


}
