package com.jabiseo.plan.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
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

    public PlanItem(Plan plan, ActivityType activityType, GoalType goalType) {
        this.plan = plan;
        this.activityType = activityType;
        this.goalType = goalType;
    }
}
