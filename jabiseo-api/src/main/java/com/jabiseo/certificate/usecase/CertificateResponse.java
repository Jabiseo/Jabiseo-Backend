package com.jabiseo.certificate.usecase;


import com.jabiseo.certificate.Certificate;

public record CertificateResponse(String certificateId, String name) {

    public static CertificateResponse from(Certificate certificate) {
        return new CertificateResponse(certificate.getCertificateId(), certificate.getName());
    }
}

