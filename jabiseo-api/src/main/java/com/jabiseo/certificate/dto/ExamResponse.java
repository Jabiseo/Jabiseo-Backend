package com.jabiseo.certificate.dto;

import com.jabiseo.certificate.domain.Exam;

public record ExamResponse(
        String examId,
        String description
) {
    public static ExamResponse from(Exam exam) {
        return new ExamResponse(exam.getId(), exam.getDescription());
    }
}
