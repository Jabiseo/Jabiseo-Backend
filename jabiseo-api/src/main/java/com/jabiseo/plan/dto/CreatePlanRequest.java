package com.jabiseo.plan.dto;

import com.jabiseo.plan.domain.ActivityType;
import com.jabiseo.plan.domain.GoalType;
import com.jabiseo.plan.domain.Plan;
import com.jabiseo.plan.domain.PlanItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record CreatePlanRequest(
        @Future
        LocalDate endDay,

        @NotNull @Valid
        List<PlanItemRequest> dailyPlan,

        @NotNull @Valid
        List<PlanItemRequest> weeklyPlan
) {

    public List<PlanItem> toPlanItems(Plan plan) {
        List<PlanItem> planItems = new ArrayList<>();
        this.dailyPlan.forEach((p) -> planItems.add(new PlanItem(plan, ActivityType.valueOf(p.activityType()), GoalType.DAILY, p.targetValue())));
        this.weeklyPlan.forEach((p) -> planItems.add(new PlanItem(plan, ActivityType.valueOf(p.activityType()), GoalType.WEEKLY, p.targetValue())));
        return planItems;
    }
}
