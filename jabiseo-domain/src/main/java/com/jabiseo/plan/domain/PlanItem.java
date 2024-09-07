package com.jabiseo.plan.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanItem {

    @Id
    @Tsid
    @Column(name = "plan_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private Integer targetValue;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public PlanItem(Plan plan, ActivityType activityType, GoalType goalType, Integer targetValue) {
        this.plan = plan;
        this.activityType = activityType;
        this.goalType = goalType;
        this.targetValue = targetValue;
    }

    public PlanProgress toPlanProgress(LocalDate progressDate) {
        return PlanProgress.builder()
                .plan(this.plan)
                .progressDate(progressDate)
                .activityType(this.activityType)
                .goalType(this.goalType)
                .targetValue(this.targetValue)
                .completedValue(0L)
                .build();
    }
}
