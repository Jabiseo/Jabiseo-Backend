package com.jabiseo.analysis.dto;

import com.jabiseo.learning.dto.TodayLearningResponse;

public record FindTodayLearningResponse(
    int studyModeCount,
    int studyModeCorrectRate,
    int examModeCount,
    int examModeCorrectRate
) {
    public static FindTodayLearningResponse from(TodayLearningResponse todayLearningResponse) {
        return new FindTodayLearningResponse(
            todayLearningResponse.studyModeCount(),
            todayLearningResponse.studyModeCorrectRate(),
            todayLearningResponse.examModeCount(),
            todayLearningResponse.examModeCorrectRate()
        );
    }
}
