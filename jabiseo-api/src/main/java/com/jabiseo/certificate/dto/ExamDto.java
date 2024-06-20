package com.jabiseo.certificate.dto;

import com.jabiseo.certificate.domain.Exam;

public record ExamDto(
        String examId,
        String description
) {
    public static ExamDto from(Exam exam) {
        return new ExamDto(exam.getId(), exam.getDescription());
    }
}
