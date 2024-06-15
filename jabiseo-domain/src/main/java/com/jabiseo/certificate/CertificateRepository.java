package com.jabiseo.certificate;

import java.util.List;

public interface CertificateRepository {

    Certificate findById(String id);

    List<Certificate> findAll();
}
