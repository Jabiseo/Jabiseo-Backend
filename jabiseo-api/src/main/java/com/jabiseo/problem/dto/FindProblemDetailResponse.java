package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

import java.util.List;

public record FindProblemDetailResponse(
        Long problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description,
        List<ChoiceResponse> choices,
        int answerNumber,
        String solution
) {
    public static FindProblemDetailResponse of(ProblemsDetailResponse problemsDetailResponse) {
        return new FindProblemDetailResponse(
                problemsDetailResponse.problemId(),
                problemsDetailResponse.examInfo(),
                problemsDetailResponse.subjectInfo(),
                problemsDetailResponse.isBookmark(),
                problemsDetailResponse.description(),
                problemsDetailResponse.choices(),
                problemsDetailResponse.answerNumber(),
                problemsDetailResponse.solution()
        );
    }
}
