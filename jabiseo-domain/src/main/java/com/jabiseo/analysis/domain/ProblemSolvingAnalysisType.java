package com.jabiseo.analysis.domain;

import com.jabiseo.analysis.exception.AnalysisBusinessException;
import com.jabiseo.analysis.exception.AnalysisErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum ProblemSolvingAnalysisType {

    SHORT_TERM(1, 0.5, 14, 200),
    MID_TERM(2, 0.3, 30, 300),
    LONG_TERM(3, 0.2, 90, 500);

    final int priority;
    final double weight;
    final int maxPeriod;
    final int maxCount;

    ProblemSolvingAnalysisType(int priority, double weight, int maxPeriod, int maxCount) {
        this.priority = priority;
        this.weight = weight;
        this.maxPeriod = maxPeriod;
        this.maxCount = maxCount;
    }

    public static ProblemSolvingAnalysisType getLongestAnalysisType() {
        return Arrays.stream(ProblemSolvingAnalysisType.values())
                .max(Comparator.comparingInt(ProblemSolvingAnalysisType::getPriority))
                .orElseThrow(() -> new IllegalStateException("No ProblemSolvingAnalysisType found"));
    }

    public static ProblemSolvingAnalysisType fromPeriodAndCount(int period, int count) {
        return Arrays.stream(ProblemSolvingAnalysisType.values())
                .sorted(Comparator.comparingInt(ProblemSolvingAnalysisType::getPriority))
                .filter(type -> period <= type.getMaxPeriod() && count <= type.getMaxCount())
                .findFirst()
                .orElseThrow(() -> new AnalysisBusinessException(AnalysisErrorCode.CANNOT_ANALYSE_PROBLEM_SOLVING));
    }

}
