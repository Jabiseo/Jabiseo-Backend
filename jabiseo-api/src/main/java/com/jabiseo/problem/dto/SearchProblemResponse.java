package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

public record SearchProblemResponse(
        Long problemId,
        Double score,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmarked,
        String description
) {
    public static SearchProblemResponse from(ProblemWithBookmarkSummaryScoreQueryDto dto) {
        return new SearchProblemResponse(
                dto.problemId(),
                dto.score(),
                ExamResponse.of(dto.examId(), dto.examDescription()),
                SubjectResponse.of(dto.subjectId(), dto.subjectSequence(), dto.subjectName()),
                dto.isBookmark(),
                dto.description()
        );
    }
}
