package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateDetailResponse;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindCertificateDetailUseCase {

    private final CertificateRepository certificateRepository;

    public FindCertificateDetailResponse execute(String id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));
        return FindCertificateDetailResponse.from(certificate);
    }
}
