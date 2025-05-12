package EduConnect.LearningApp.backend.features.learningPlan.repository;

import EduConnect.LearningApp.backend.features.learningPlan.model.PlanTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanTemplateRepository extends JpaRepository<PlanTemplate, Long> {

}

