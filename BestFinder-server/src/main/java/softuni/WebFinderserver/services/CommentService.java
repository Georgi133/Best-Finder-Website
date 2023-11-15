package softuni.WebFinderserver.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.Comment;
import softuni.WebFinderserver.model.views.CommentView;
import softuni.WebFinderserver.repositories.CommentRepository;
import softuni.WebFinderserver.services.exceptions.torrent.TorrentException;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentView mapToView(Comment comment) {
       return CommentView.builder()
               .id(comment.getId())
               .fullName(comment.getCreatedBy().getFullName())
                .comment(comment.getText())
                .userEmail(comment.getCreatedBy().getEmail())
                .build();
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    public void editCommentById(Long id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("No such comment when editing the comment"));
        comment.setText(text);
        commentRepository.save(comment);
    }

    public boolean isOwnerOfComment (Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new TorrentException("Such comment in torrent does not exist", HttpStatus.BAD_REQUEST));

        if(comment.getCreatedBy().getId() == userId) {
            return true;
        }
        return false;

    }

}
