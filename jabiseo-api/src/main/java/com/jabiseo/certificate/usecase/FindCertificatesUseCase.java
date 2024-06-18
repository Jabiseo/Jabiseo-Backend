package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCertificatesUseCase {

    private final CertificateRepository certificateRepository;

    public List<FindCertificatesResponse> execute() {
        return certificateRepository
                .findAll()
                .stream()
                .map(FindCertificatesResponse::from)
                .toList();
    }
}
