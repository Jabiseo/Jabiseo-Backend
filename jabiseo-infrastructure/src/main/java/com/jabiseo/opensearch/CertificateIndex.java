package com.jabiseo.opensearch;

import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.common.exception.CommonErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public enum CertificateIndex {

    JEONGBOCHEORIGISA("jeongbocheorigisa", 1L),
    ;

    // indexName을 반환하는 getter 메소드
    @Getter
    private String indexName;
    private Long certificateId;

    CertificateIndex(String indexName, Long certificateId) {
        this.indexName = indexName;
        this.certificateId = certificateId;
    }

    public static CertificateIndex findByCertificateId(Long certificateId) {
        return Arrays.stream(CertificateIndex.values())
                .filter(certificate -> Objects.equals(certificate.certificateId, certificateId))
                .findFirst()
                .orElseThrow(() -> new CertificateBusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}
