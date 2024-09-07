package com.jabiseo.learning.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProblemSolvingRepository extends JpaRepository<ProblemSolving, Long> {

    List<ProblemSolving> findByMemberAndLearning_CertificateAndLearning_CreatedAtAfter(Member member, Certificate learning_certificate, LocalDateTime createdAt);

}
