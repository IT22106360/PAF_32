package EduConnect.LearningApp.backend.features.notifications.repository;


import EduConnect.LearningApp.backend.features.authentication.model.User;
import EduConnect.LearningApp.backend.features.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);

    List<Notification> findByRecipientOrderByCreationDateDesc(User user);
}
