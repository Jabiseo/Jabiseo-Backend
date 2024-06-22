package com.jabiseo.certificate.dto;


import com.jabiseo.certificate.domain.Certificate;

public record FindCertificateListResponse(
        String certificateId,
        String name
) {
    public static FindCertificateListResponse from(Certificate certificate) {
        return new FindCertificateListResponse(certificate.getId(), certificate.getName());
    }
}
