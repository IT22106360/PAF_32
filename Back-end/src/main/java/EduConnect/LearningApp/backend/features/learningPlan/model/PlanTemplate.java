package EduConnect.LearningApp.backend.features.learningPlan.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;


import java.time.LocalDateTime;


@Entity(name = "plan_templates")
@Indexed(index = "plan_templates")
public class PlanTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @FullTextField(analyzer = "standard")
    private String name;

    @FullTextField(analyzer = "standard")
    private String description;

    @NotNull
    private Integer suggestedDurationDays;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PlanTemplate() {}

    public PlanTemplate(String name, String description, Integer suggestedDurationDays) {
        this.name = name;
        this.description = description;
        this.suggestedDurationDays = suggestedDurationDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSuggestedDurationDays() {
        return suggestedDurationDays;
    }

    public void setSuggestedDurationDays(Integer suggestedDurationDays) {
        this.suggestedDurationDays = suggestedDurationDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
