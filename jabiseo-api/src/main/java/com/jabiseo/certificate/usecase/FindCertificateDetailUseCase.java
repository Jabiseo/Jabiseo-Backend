package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindCertificateDetailUseCase {

    private final CertificateRepository certificateRepository;

    public FindCertificateDetailResponse execute(String id) {
        return FindCertificateDetailResponse.from(certificateRepository.findById(id));
    }
}
