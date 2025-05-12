package EduConnect.LearningApp.backend.features.learningPlan.controller;
import EduConnect.LearningApp.backend.features.authentication.model.User;
import EduConnect.LearningApp.backend.features.learningPlan.model.PlanComment;
import EduConnect.LearningApp.backend.features.learningPlan.service.PlanCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans/{planId}/comments")
public class PlanCommentController {
    private final PlanCommentService commentService;

    public PlanCommentController(PlanCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<PlanComment> addComment(@PathVariable Long planId,
                                                  @RequestBody String content,
                                                  @RequestAttribute("currentUser") User currentUser) {
        PlanComment comment = commentService.addComment(planId, content, currentUser);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<PlanComment>> getComments(@PathVariable Long planId) {
        return ResponseEntity.ok(commentService.getCommentsForPlan(planId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long planId,
                                              @PathVariable Long commentId,
                                              @RequestAttribute("currentUser") User currentUser,
                                              @RequestAttribute("isAdmin") boolean isAdmin) {
        PlanComment comment = commentService.getCommentsForPlan(planId).stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));
        commentService.deleteComment(comment, currentUser, isAdmin);
        return ResponseEntity.ok().build();
    }
}

