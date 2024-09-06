package com.jabiseo.plan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlanProgressRepository extends JpaRepository<PlanProgress, Long> {

    List<PlanProgress> findAllByPlanAndProgressDateBetweenOrderByProgressDate(Plan plan, LocalDate start, LocalDate end);

    List<PlanProgress> findAllByProgressDateBetweenAndGoalType(LocalDate start, LocalDate end, GoalType goalType);
}
