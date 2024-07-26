package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.domain.Problem;

import java.util.List;

public record ProblemsDetailResponse(
        String problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description,
        List<ChoiceResponse> choices,
        int answerNumber,
        String solution
) {
    public static ProblemsDetailResponse from(Problem problem) {
        return new ProblemsDetailResponse(
                problem.getId(),
                ExamResponse.from(problem.getExam()),
                SubjectResponse.from(problem.getSubject()),
                false, // TODO: 로그인 기능 구현 후 수정
                problem.getDescription(),
                ChoiceResponse.fromChoices(problem.getChoices()),
                problem.getAnswerNumber(),
                problem.getSolution()
        );
    }
}
