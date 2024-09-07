package com.jabiseo.learning.domain;

import com.jabiseo.learning.domain.querydsl.LearningQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningRepository extends JpaRepository<Learning, Long>, LearningQueryDslRepository {
}
