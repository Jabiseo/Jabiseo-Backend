package com.jabiseo.plan.dto;

import com.jabiseo.common.validator.EnumValid;
import com.jabiseo.plan.domain.ActivityType;

public record PlanItemRequest (
        int targetValue,
        @EnumValid(enumClass = ActivityType.class, message = "허용된 plan activity 가 아닙니다")
        String activityType
){
}
