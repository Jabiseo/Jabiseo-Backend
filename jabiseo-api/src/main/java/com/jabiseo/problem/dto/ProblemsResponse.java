package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.domain.Problem;

public record ProblemsResponse(
        Long problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description
) {
    public static ProblemsResponse from(Problem problem) {
        return new ProblemsResponse(
                problem.getId(),
                ExamResponse.from(problem.getExam()),
                SubjectResponse.from(problem.getSubject()),
                false, // TODO: 로그인 기능 구현 후 수정
                problem.getDescription()
        );
    }
}
