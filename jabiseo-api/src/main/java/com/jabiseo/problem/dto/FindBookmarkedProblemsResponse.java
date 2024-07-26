package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.domain.Problem;

public record FindBookmarkedProblemsResponse(
        String problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description
) {
    public static FindBookmarkedProblemsResponse from(Problem problem) {
        return new FindBookmarkedProblemsResponse(
                problem.getId(),
                ExamResponse.from(problem.getExam()),
                SubjectResponse.from(problem.getSubject()),
                false, //TODO: 로그인 기능 구현 후 수정
                problem.getDescription()
        );
    }
}
