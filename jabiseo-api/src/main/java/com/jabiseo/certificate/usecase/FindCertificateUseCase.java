package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCertificateUseCase {

    private final CertificateRepository certificateRepository;

    public List<CertificateResponse> findAll() {
        return certificateRepository
                .findAll()
                .stream()
                .map(CertificateResponse::from)
                .toList();
    }
}
