package com.jabiseo.certificate.dto;

public record SubjectResponse(
        String subjectId,
        int sequence,
        String name
) {
    public static SubjectResponse from(com.jabiseo.certificate.domain.Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getSequence(), subject.getName());
    }
}
