package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

import java.util.List;

public record FindProblemsResponse(
        String problemId,
        ExamResponse examInfo,
        SubjectResponse subject,
        boolean isBookmark,
        String description,
        List<ChoiceResponse> choices,
        int answerNumber,
        String theory,
        String solution
) {
}
