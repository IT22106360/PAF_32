package EduConnect.LearningApp.backend.features.learningPlan.controller;
import EduConnect.LearningApp.backend.features.learningPlan.model.PlanTemplate;
import EduConnect.LearningApp.backend.features.learningPlan.service.PlanTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class PlanTemplateController {
    private final PlanTemplateService templateService;

    public PlanTemplateController(PlanTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<PlanTemplate> createTemplate(@RequestBody PlanTemplate template) {
        return ResponseEntity.ok(templateService.createTemplate(template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanTemplate> getTemplate(@PathVariable Long id) {
        return templateService.getTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PlanTemplate>> searchTemplates(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(templateService.searchTemplates(q == null ? "" : q));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }
}

