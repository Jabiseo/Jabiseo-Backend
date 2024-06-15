package com.jabiseo.database;

import com.jabiseo.certificate.Certificate;
import com.jabiseo.certificate.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CertificateRepositoryImpl implements CertificateRepository {

    private final JpaCertifcateRepository jpaCertifcateRepository;

    @Override
    public Certificate findById(String id) {
        JpaCertificateEntity jpaEntity = jpaCertifcateRepository
                .findById(id)
                .orElseThrow();

        return CertificateMapper.jpaToDomain(jpaEntity);
    }

    @Override
    public List<Certificate> findAll() {
        return jpaCertifcateRepository
                .findAll()
                .stream()
                .map(CertificateMapper::jpaToDomain)
                .toList();
    }
}
