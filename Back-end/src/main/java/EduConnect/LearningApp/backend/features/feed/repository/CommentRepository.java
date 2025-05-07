package EduConnect.LearningApp.backend.features.feed.repository;


import EduConnect.LearningApp.backend.features.feed.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}