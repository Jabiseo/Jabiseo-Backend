package com.jabiseo.certificate.dto;


import com.jabiseo.certificate.domain.Certificate;

public record FindCertificatesResponse(
        String certificateId,
        String name
) {
    public static FindCertificatesResponse from(Certificate certificate) {
        return new FindCertificatesResponse(certificate.getId(), certificate.getName());
    }
}
