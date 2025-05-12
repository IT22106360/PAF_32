package EduConnect.LearningApp.backend.features.learningPlan.service;

import EduConnect.LearningApp.backend.features.learningPlan.model.PlanTemplate;
import EduConnect.LearningApp.backend.features.learningPlan.repository.PlanTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanTemplateService {
    private final PlanTemplateRepository templateRepo;

    public PlanTemplateService(PlanTemplateRepository templateRepo) {
        this.templateRepo = templateRepo;
    }

    public PlanTemplate createTemplate(PlanTemplate template) {
        return templateRepo.save(template);
    }

    public Optional<PlanTemplate> getTemplateById(Long id) {
        return templateRepo.findById(id);
    }

    public List<PlanTemplate> searchTemplates(String keyword) {
        return templateRepo.findAll().stream()
                .filter(t -> t.getName().contains(keyword) || t.getDescription().contains(keyword))
                .toList();
    }

    public void deleteTemplate(Long id) {
        templateRepo.deleteById(id);
    }
}

