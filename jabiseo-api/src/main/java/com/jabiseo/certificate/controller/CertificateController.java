package com.jabiseo.certificate.controller;

import com.jabiseo.certificate.usecase.CertificateResponse;
import com.jabiseo.certificate.usecase.FindCertificateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CertificateController {

    private final FindCertificateUseCase findCertificateUseCase;

    @GetMapping("/api/certificate")
    public ResponseEntity<?> findCertificate() {
        List<CertificateResponse> result = findCertificateUseCase.execute();
        return ResponseEntity.ok(result);
    }
}
