package com.jabiseo.learning.domain.querydsl;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.dto.LearningWithSolvingCountQueryDto;
import com.jabiseo.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

public interface LearningQueryDslRepository {

    List<LearningWithSolvingCountQueryDto> findLearningWithSolvingCount(Member member, Certificate certificate, LocalDate startDate, LocalDate endDate);
}
