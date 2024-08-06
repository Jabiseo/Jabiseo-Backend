package com.jabiseo.problem.domain;

import com.jabiseo.problem.domain.querydsl.ProblemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends ProblemRepositoryCustom, JpaRepository<Problem, Long> {
}
