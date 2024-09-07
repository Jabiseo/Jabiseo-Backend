package com.jabiseo.analysis.dto;

public record FindVulnerableSubjectResponse(
    Long subjectId,
    String subjectName,
    int vulnerabilityScore
) {
    public static FindVulnerableSubjectResponse from(VulnerableSubjectResponse vulnerableSubjectResponse) {
        return new FindVulnerableSubjectResponse(
            vulnerableSubjectResponse.subjectId(),
            vulnerableSubjectResponse.subjectName(),
            vulnerableSubjectResponse.vulnerableRate()
        );
    }
}
