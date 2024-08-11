package com.jabiseo.plan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreatePlanRequest(
        @Future
        LocalDate endDay,

        @NotNull @Valid
        List<PlanItemRequest> dailyPlan,

        @NotNull @Valid
        List<PlanItemRequest> weeklyPlan
) {
}
