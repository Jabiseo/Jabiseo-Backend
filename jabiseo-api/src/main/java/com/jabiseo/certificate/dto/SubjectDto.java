package com.jabiseo.certificate.dto;

public record SubjectDto(
        String subjectId,
        int sequence,
        String name
) {
    public static SubjectDto from(com.jabiseo.certificate.domain.Subject subject) {
        return new SubjectDto(subject.getId(), subject.getSequence(), subject.getName());
    }
}
