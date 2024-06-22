package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

public record FindBookmarkedProblemsResponse(
        String problemId,
        ExamResponse examInfo,
        SubjectResponse subject,
        boolean isBookmark,
        String description
) {
}
