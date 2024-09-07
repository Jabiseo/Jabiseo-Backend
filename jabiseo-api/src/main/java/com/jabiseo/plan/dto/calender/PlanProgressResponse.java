package com.jabiseo.plan.dto.calender;

import com.jabiseo.plan.domain.ActivityType;

public record PlanProgressResponse(
        ActivityType activityType,
        Long completedValue,
        Integer targetValue
) {
}
