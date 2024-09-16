package com.jabiseo.plan.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    boolean existsByCertificateAndMemberAndEndAtAfter(Certificate certificate, Member member, LocalDate now);
    Optional<Plan> findFirstByCertificateAndMember(Certificate certificate, Member member);

    @Query("SELECT p FROM Plan p JOIN FETCH p.planItems WHERE p.id = :planId")
    Optional<Plan> findPlanWithItemsById(@Param("planId") Long planId);

}
