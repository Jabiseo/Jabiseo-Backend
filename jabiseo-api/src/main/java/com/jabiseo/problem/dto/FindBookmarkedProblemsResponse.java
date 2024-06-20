package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamDto;
import com.jabiseo.certificate.dto.SubjectDto;

import java.util.List;

public record FindBookmarkedProblemsResponse(
        String problemId,
        ExamDto examInfo,
        SubjectDto subject,
        boolean isBookmark,
        String description
) {
}
