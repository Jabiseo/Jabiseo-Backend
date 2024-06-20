package com.jabiseo.learning.dto;

import com.jabiseo.learning.domain.LearningMode;

public record CreateLearningRequest(
        long studyTime,
        LearningMode learningMode,
        ProblemResultDto problems
) {
}
