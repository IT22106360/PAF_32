package EduConnect.LearningApp.backend.features.learningPlan.service;
import EduConnect.LearningApp.backend.features.authentication.model.User;
import EduConnect.LearningApp.backend.features.authentication.repository.UserRepository;
import EduConnect.LearningApp.backend.features.learningPlan.model.LearningPlan;
import EduConnect.LearningApp.backend.features.learningPlan.repository.LearningPlanRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class LearningPlanService {
    private final LearningPlanRepository planRepo;
    private final UserRepository userRepository;

    public LearningPlanService(LearningPlanRepository planRepo, UserRepository userRepository) {
        this.planRepo = planRepo;
        this.userRepository = userRepository;
    }

    public LearningPlan createPlan(LearningPlan plan) {
        return planRepo.save(plan);
    }

    public Optional<LearningPlan> getPlanById(Long id) {
        return planRepo.findById(id);
    }

    public List<LearningPlan> getPlansByOwner(Long owner) {
        return planRepo.findByOwnerId(owner);
    }


    @Transactional
    public LearningPlan updatePlan(Long planId,
                                   LearningPlan updatedData,
                                   Long userId) {
        LearningPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Learning plan not found"));

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isOwner = plan.getOwner().equals(currentUser);
        boolean isCollaborator = plan.getCollaborators().contains(currentUser);
        if (!isOwner && !isCollaborator) {
            throw new IllegalArgumentException("You do not have permission to update this plan.");
        }

        plan.setTitle(updatedData.getTitle());
        plan.setDescription(updatedData.getDescription());
        plan.setStartDate(updatedData.getStartDate());
        plan.setEndDate(updatedData.getEndDate());
        plan.setStatus(updatedData.getStatus());

        return planRepo.save(plan);
    }

    /**
     * Delete a plan by ID.
     * Optionally enforce authorization if desired.
     */
    public void deletePlan(Long planId, Long userId) {
        LearningPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Learning plan not found"));

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isOwner = plan.getOwner().equals(currentUser);
        boolean isCollaborator = plan.getCollaborators().contains(currentUser);
        if (!isOwner && !isCollaborator) {
            throw new IllegalArgumentException("You do not have permission to delete this plan.");
        }

        planRepo.delete(plan);
    }
}

