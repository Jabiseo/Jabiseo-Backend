package com.jabiseo.problem.dto;

public record ProblemWithBookmarkSummaryDto(
        Long problemId,
        String description,
        boolean isBookmark,

        Long examId,
        String examDescription,

        Long subjectId,
        String subjectName,
        int subjectSequence
) {
}
