package com.jabiseo.certificate.controller;

import com.jabiseo.certificate.dto.FindCertificatesResponse;
import com.jabiseo.certificate.dto.FindCertificateResponse;
import com.jabiseo.certificate.usecase.FindCertificateUseCase;
import com.jabiseo.certificate.usecase.FindCertificatesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {

    private final FindCertificatesUseCase findCertificatesUseCase;

    private final FindCertificateUseCase findCertificateUseCase;

    @GetMapping
    public ResponseEntity<List<FindCertificatesResponse>> findCertificates() {
        List<FindCertificatesResponse> result = findCertificatesUseCase.execute();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{certificate-id}")
    public ResponseEntity<FindCertificateResponse> findCertificate(
            @PathVariable(name = "certificate-id") String certificateId
    ) {
        FindCertificateResponse result = findCertificateUseCase.execute(certificateId);
        return ResponseEntity.ok(result);
    }
}
