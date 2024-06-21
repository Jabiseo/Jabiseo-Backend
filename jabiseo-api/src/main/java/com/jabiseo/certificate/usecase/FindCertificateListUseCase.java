package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindCertificateListUseCase {

    private final CertificateRepository certificateRepository;

    public List<FindCertificateListResponse> execute() {
        return certificateRepository
                .findAll()
                .stream()
                .map(FindCertificateListResponse::from)
                .toList();
    }
}
