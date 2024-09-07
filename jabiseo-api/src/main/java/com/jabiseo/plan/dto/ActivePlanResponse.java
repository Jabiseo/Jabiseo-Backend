package com.jabiseo.plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jabiseo.problem.dto.CertificateResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ActivePlanResponse(
        String planId,
        CertificateResponse certificate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endAt,
        List<PlanItemResponse> dailyPlanItems,
        List<PlanItemResponse> weeklyPlanItems
) {
}
