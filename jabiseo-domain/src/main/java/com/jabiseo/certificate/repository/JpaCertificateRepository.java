package com.jabiseo.certificate.repository;

import com.jabiseo.certificate.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCertificateRepository extends JpaRepository<Certificate, String> {
}
