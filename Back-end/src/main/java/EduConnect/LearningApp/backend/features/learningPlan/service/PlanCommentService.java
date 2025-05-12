package EduConnect.LearningApp.backend.features.learningPlan.service;

import EduConnect.LearningApp.backend.features.authentication.model.User;
import EduConnect.LearningApp.backend.features.authentication.repository.UserRepository;
import EduConnect.LearningApp.backend.features.learningPlan.model.LearningPlan;
import EduConnect.LearningApp.backend.features.learningPlan.model.PlanComment;
import EduConnect.LearningApp.backend.features.learningPlan.repository.LearningPlanRepository;
import EduConnect.LearningApp.backend.features.learningPlan.repository.PlanCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanCommentService {
    private final PlanCommentRepository commentRepo;
    private final LearningPlanRepository planRepo;
    private final UserRepository userRepo;

    public PlanCommentService(PlanCommentRepository commentRepo,
                              LearningPlanRepository planRepo,
                              UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.planRepo = planRepo;
        this.userRepo = userRepo;
    }

    /**
     * Add a new comment to a learning plan. The author is taken from the authenticated user.
     */
    @Transactional
    public PlanComment addComment(Long planId, String content, User currentUser) {
        User author = userRepo.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        LearningPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Learning plan not found."));

        // Only owner or collaborators may comment
        if (!plan.getOwner().equals(author) && !plan.getCollaborators().contains(author)) {
            throw new IllegalArgumentException("You do not have permission to comment on this plan.");
        }

        PlanComment comment = new PlanComment(content, plan, author);
        return commentRepo.save(comment);
    }

    /**
     * Delete a comment. Only author or ADMIN may delete.
     */
    public void deleteComment(PlanComment comment, User currentUser, boolean isAdmin) {
        if (!comment.getAuthor().equals(currentUser) && !isAdmin) {
            throw new IllegalArgumentException("You do not have permission to delete this comment.");
        }
        commentRepo.delete(comment);
    }

    public List<PlanComment> getCommentsForPlan(Long planId) {
        return commentRepo.findAll().stream()
                .filter(c -> c.getLearningPlan().getId().equals(planId))
                .toList();
    }
}
