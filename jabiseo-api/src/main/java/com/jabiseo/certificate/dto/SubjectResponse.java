package com.jabiseo.certificate.dto;

import com.jabiseo.certificate.domain.Subject;

public record SubjectResponse(
        Long subjectId,
        int sequence,
        String name
) {
    public static SubjectResponse from(Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getSequence(), subject.getName());
    }
}
