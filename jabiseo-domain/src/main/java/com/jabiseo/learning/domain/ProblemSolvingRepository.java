package com.jabiseo.learning.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProblemSolvingRepository extends JpaRepository<ProblemSolving, Long> {

    @Query("select ps from ProblemSolving ps join fetch ps.learning l where ps.member = :member and l.certificate = :learning_certificate and l.createdAt > :createdAt")
    List<ProblemSolving> findByMemberAndCertificateAndCreatedAtAfterWithLearning(Member member, Certificate learning_certificate, LocalDateTime createdAt);

}
