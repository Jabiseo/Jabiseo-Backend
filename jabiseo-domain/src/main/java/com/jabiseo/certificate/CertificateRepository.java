package com.jabiseo.certificate;

import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CertificateRepository {

    private final JpaCertificateRepository jpaCertificateRepository;

    public Certificate findById(String id) {
        return jpaCertificateRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));
    }

    public List<Certificate> findAll() {
        return jpaCertificateRepository.findAll();
    }
}
