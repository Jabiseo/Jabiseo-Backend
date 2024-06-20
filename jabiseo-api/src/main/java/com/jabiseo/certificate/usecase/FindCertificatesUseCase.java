package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
