package com.jabiseo.plan.dto.calender;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlanProgressDateResponse(
        LocalDate day,
        Integer week,
        List<PlanProgressResponse> planItems
) {

    public static PlanProgressDateResponse ofWeek(Integer week, List<PlanProgressResponse> planItems) {
        return new PlanProgressDateResponse(null, week, planItems);
    }

    public static PlanProgressDateResponse ofDay(LocalDate day, List<PlanProgressResponse> planItems) {
        return new PlanProgressDateResponse(day, null, planItems);
    }
}
