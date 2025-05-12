package EduConnect.LearningApp.backend.features.learningPlan.controller;
import EduConnect.LearningApp.backend.features.authentication.model.User;
import EduConnect.LearningApp.backend.features.learningPlan.model.LearningPlan;
import EduConnect.LearningApp.backend.features.learningPlan.model.PlanTemplate;
import EduConnect.LearningApp.backend.features.learningPlan.service.LearningPlanService;
import EduConnect.LearningApp.backend.features.learningPlan.service.PlanTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class LearningPlanController {
    private final LearningPlanService planService;
    private final PlanTemplateService templateService;

    public LearningPlanController(LearningPlanService planService,
                                  PlanTemplateService templateService) {
        this.planService = planService;
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<LearningPlan> createPlan(@RequestBody LearningPlan plan,
                                                   @RequestAttribute("authenticatedUser") User user) {
        plan.setOwner(user);
        return ResponseEntity.ok(planService.createPlan(plan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPlan> getPlan(@PathVariable Long id) {
        return planService.getPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPlan> updatePlan(
            @PathVariable Long id,
            @RequestBody LearningPlan updated,
            @RequestParam("userId") Long userId) {
        LearningPlan result = planService.updatePlan(id, updated, userId);
        return ResponseEntity.ok(result);
    }

    /** Delete a plan; requires userId query parameter */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {
        planService.deletePlan(id, userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<LearningPlan>> getAllPlans(
             @RequestAttribute("authenticatedUser") Long user) {
        List<LearningPlan> plans = planService.getPlansByOwner(user);
        return ResponseEntity.ok(plans);
    }

    // AI-driven recommendations endpoint
    @GetMapping("/recommendations")
    public ResponseEntity<List<PlanTemplate>> getRecommendations( @RequestAttribute("authenticatedUser") User user) {
        // TODO: replace with AI-based logic; currently returns all templates
        List<PlanTemplate> templates = templateService.searchTemplates("");
        return ResponseEntity.ok(templates);
    }
}

