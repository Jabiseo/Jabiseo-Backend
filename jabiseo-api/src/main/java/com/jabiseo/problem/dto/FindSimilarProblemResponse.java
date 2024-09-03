package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

public record FindSimilarProblemResponse(
        Long problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description
) {
    public static FindSimilarProblemResponse of(ProblemWithBookmarkSummaryQueryDto dto) {
        return new FindSimilarProblemResponse(
                dto.problemId(),
                ExamResponse.of(dto.examId(), dto.examDescription()),
                SubjectResponse.of(dto.subjectId(), dto.subjectSequence(), dto.subjectName()),
                dto.isBookmark(),
                dto.description()
        );
    }
}
