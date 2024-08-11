package com.jabiseo.plan.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    boolean existsByCertificateAndMemberAndEndAtAfter(Certificate certificate, Member member, LocalDate now);
}
