package com.jabiseo.learning.dto;

public record TodayLearningDto(
    int studyModeCount,
    int studyModeCorrectRate,
    int examModeCount,
    int examModeCorrectRate
) {
    public static TodayLearningDto of(
        int studyModeCount,
        int studyModeCorrectRate,
        int examModeCount,
        int examModeCorrectRate
    ) {
        return new TodayLearningDto(
            studyModeCount,
            studyModeCorrectRate,
            examModeCount,
            examModeCorrectRate
        );
    }
}
