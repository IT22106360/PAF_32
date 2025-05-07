package EduConnect.LearningApp.backend.features.messaging.repository;


import EduConnect.LearningApp.backend.features.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}