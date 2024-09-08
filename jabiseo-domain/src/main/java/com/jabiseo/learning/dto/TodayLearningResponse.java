package com.jabiseo.learning.dto;

public record TodayLearningResponse(
    int studyModeCount,
    int studyModeCorrectRate,
    int examModeCount,
    int examModeCorrectRate
) {
    public static TodayLearningResponse of(
        int studyModeCount,
        int studyModeCorrectRate,
        int examModeCount,
        int examModeCorrectRate
    ) {
        return new TodayLearningResponse(
            studyModeCount,
            studyModeCorrectRate,
            examModeCount,
            examModeCorrectRate
        );
    }
}
