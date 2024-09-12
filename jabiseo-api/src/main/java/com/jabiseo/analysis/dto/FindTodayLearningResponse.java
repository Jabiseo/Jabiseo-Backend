package com.jabiseo.analysis.dto;

import com.jabiseo.learning.dto.TodayLearningDto;

public record FindTodayLearningResponse(
    int studyModeCount,
    int studyModeCorrectRate,
    int examModeCount,
    int examModeCorrectRate
) {
    public static FindTodayLearningResponse from(TodayLearningDto todayLearningDto) {
        return new FindTodayLearningResponse(
            todayLearningDto.studyModeCount(),
            todayLearningDto.studyModeCorrectRate(),
            todayLearningDto.examModeCount(),
            todayLearningDto.examModeCorrectRate()
        );
    }
}
