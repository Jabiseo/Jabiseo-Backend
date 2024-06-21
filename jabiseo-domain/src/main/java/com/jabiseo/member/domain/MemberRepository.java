package com.jabiseo.member.domain;

import com.jabiseo.certificate.domain.Certificate;

public interface MemberRepository {

    Member findById(String id);

    Certificate findCertificateStateById(String id);
}
