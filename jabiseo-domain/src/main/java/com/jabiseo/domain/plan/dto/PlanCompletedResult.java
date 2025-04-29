package com.jabiseo.domain.plan.dto;

import lombok.Builder;

public class PlanCompletedResult {
    private Long id;
    private boolean isCompleted;
    private boolean isProgressEmpty;

    @Builder
    public PlanCompletedResult(Long id, boolean isCompleted, boolean isProgressEmpty) {
        this.id = id;
        this.isCompleted = isCompleted;
        this.isProgressEmpty = isProgressEmpty;
    }

    public static PlanCompletedResult empty(Long id) {
        return PlanCompletedResult.builder()
                .id(id)
                .isCompleted(false)
                .isProgressEmpty(true)
                .build();
    }

    public static PlanCompletedResult completed(Long id, boolean isCompleted) {
        return PlanCompletedResult.builder()
                .id(id)
                .isCompleted(isCompleted)
                .isProgressEmpty(false)
                .build();
    }

    public Long getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isProgressEmpty() {
        return isProgressEmpty;
    }
}
