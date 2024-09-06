package com.jabiseo.plan.dto.calender;

import com.jabiseo.plan.domain.ActivityType;
import lombok.Getter;

@Getter
public class PlanProgressResponse {
    ActivityType activityType;
    Integer completedValue;
    Integer targetValue;

    public PlanProgressResponse(ActivityType activityType, Integer completedValue, Integer targetValue) {
        this.activityType = activityType;
        this.completedValue = completedValue;
        this.targetValue = targetValue;
    }

}
