package com.jabiseo.member.domain;

import com.jabiseo.certificate.domain.Certificate;

public interface MemberRepository {

    Certificate findCertificateStateById(String id);
}
