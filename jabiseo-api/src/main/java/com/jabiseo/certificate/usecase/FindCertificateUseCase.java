package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindCertificateUseCase {

    private final CertificateRepository certificateRepository;

    public FindCertificateResponse execute(String id) {
        return FindCertificateResponse.from(certificateRepository.findById(id));
    }
}
