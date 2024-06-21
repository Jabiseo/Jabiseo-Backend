package com.jabiseo.certificate.dto;

import com.jabiseo.certificate.domain.Certificate;

import java.util.List;

public record FindCertificateDetailResponse(
        String certificateId,
        String name,
        List<ExamDto> exams,
        List<SubjectDto> subjects
) {
    public static FindCertificateDetailResponse from(Certificate certificate) {
        return new FindCertificateDetailResponse(
                certificate.getId(),
                certificate.getName(),
                certificate.getExams()
                        .stream()
                        .map(ExamDto::from)
                        .toList(),
                certificate.getSubjects()
                        .stream()
                        .map(SubjectDto::from)
                        .toList()
        );
    }
}
