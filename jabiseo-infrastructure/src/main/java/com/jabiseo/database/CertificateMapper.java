package com.jabiseo.database;

import com.jabiseo.certificate.Certificate;


public class CertificateMapper {

    static Certificate jpaToDomain(JpaCertificateEntity entity) {
        return Certificate.builder()
                .certificateId(entity.getId())
                .name(entity.getName())
                .build();
    }

    static JpaCertificateEntity domainToJpa(Certificate domain) {
        return JpaCertificateEntity.builder()
                .id(domain.getCertificateId())
                .name(domain.getCertificateId())
                .build();
    }

}
