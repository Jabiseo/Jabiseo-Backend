package com.jabiseo.analysis.dto;

public record VulnerableSubjectResponse(
        Long subjectId,
        String subjectName,
        int vulnerableRate
) {
    public static VulnerableSubjectResponse of(Long subjectId, String subjectName, int vulnerableRate) {
        return new VulnerableSubjectResponse(subjectId, subjectName, vulnerableRate);
    }
}
