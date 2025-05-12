package EduConnect.LearningApp.backend.features.learningPlan.repository;

import EduConnect.LearningApp.backend.features.learningPlan.model.PlanComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCommentRepository extends JpaRepository<PlanComment, Long> {

}
