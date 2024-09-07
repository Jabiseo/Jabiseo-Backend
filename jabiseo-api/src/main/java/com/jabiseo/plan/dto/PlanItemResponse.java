package com.jabiseo.plan.dto;

import com.jabiseo.plan.domain.ActivityType;
import com.jabiseo.plan.domain.PlanItem;

public record PlanItemResponse(
        Long planItemId,
        ActivityType activityType,
        int targetValue
) {

    public static PlanItemResponse from(PlanItem planItem) {
        return new PlanItemResponse(planItem.getId(), planItem.getActivityType(), planItem.getTargetValue());
    }
}
