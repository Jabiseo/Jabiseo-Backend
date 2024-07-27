package com.jabiseo.problem.dto;

import com.jabiseo.certificate.domain.Certificate;

public record CertificateResponse(
        String certificateId,
        String name
) {
    public static CertificateResponse from(Certificate certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getName()
        );
    }
}
