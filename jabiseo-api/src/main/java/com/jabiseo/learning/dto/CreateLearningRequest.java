package com.jabiseo.learning.dto;

import com.jabiseo.learning.domain.LearningMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateLearningRequest(
        @Min(value = 1L, message = "학습 시간은 0보다 커야 합니다.")
        Long learningTime,
        LearningMode learningMode,
        Long certificateId,
        @NotEmpty(message = "문제를 풀어야 합니다.")
        List<ProblemResultRequest> problems
) {
}
