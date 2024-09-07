package com.jabiseo.analysis.dto;

public record FindTodayLearningResponse(
    int studyModeCount,
    int studyModeCorrectRate,
    int examModeCount,
    int examModeCorrectRate
) {
    public static FindTodayLearningResponse of(
        int studyModeCount,
        int studyModeCorrectRate,
        int examModeCount,
        int examModeCorrectRate
    ) {
        return new FindTodayLearningResponse(
            studyModeCount,
            studyModeCorrectRate,
            examModeCount,
            examModeCorrectRate
        );
    }
}
