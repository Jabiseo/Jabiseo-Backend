package com.jabiseo.plan.domain;


import com.jabiseo.learning.domain.LearningMode;
import com.jabiseo.learning.domain.LearningRepository;
import com.jabiseo.learning.dto.LearningWithSolvingCountQueryDto;
import com.jabiseo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanProgressService {

    private final WeeklyDefineStrategy weeklyDefineStrategy;
    private final PlanProgressRepository planProgressRepository;
    private final LearningRepository learningRepository;

    public void createCurrentPlanProgress(Member member, List<PlanItem> planItems) {
        WeekPeriod currentWeekPeriod = weeklyDefineStrategy.getCurrentWeekPeriod(LocalDate.now());

        List<PlanProgress> daily = planItems.stream().filter(p -> p.getGoalType().equals(GoalType.DAILY))
                .map(planItem -> planItem.toPlanProgress(LocalDate.now()))
                .toList();

        List<PlanProgress> weekly = planItems.stream().filter(p -> p.getGoalType().equals(GoalType.WEEKLY))
                .map(planItem -> planItem.toPlanProgress(currentWeekPeriod.getEnd()))
                .toList();

        learningCalculateAndSave(member, daily, LocalDate.now(), LocalDate.now());
        learningCalculateAndSave(member, weekly, currentWeekPeriod.getStart(), currentWeekPeriod.getEnd());
    }

    private void learningCalculateAndSave(Member member, List<PlanProgress> progress, LocalDate start, LocalDate end) {
        // 없으면 진행하지 않는다.
        if (progress.isEmpty()) {
            return;
        }

        List<LearningWithSolvingCountQueryDto> queryResult = learningRepository.findLearningWithSolvingCount(member, member.getCurrentCertificate(), start, end);
        if(queryResult.isEmpty()) {
            return;
        }

        List<PlanProgress> planProgresses = calculateProgress(progress, queryResult);
        planProgressRepository.saveAll(planProgresses);
    }

    // 테스트를 위해 package-private 구성
    List<PlanProgress> calculateProgress(List<PlanProgress> progress, List<LearningWithSolvingCountQueryDto> learningWithSolving) {
        // PlanProgress 가 ActivityType 당 1개임을 보장하기에 이렇게 구성
        Map<ActivityType, PlanProgress> map = progress.stream()
                .collect(Collectors.toMap(
                        PlanProgress::getActivityType,
                        Function.identity()
                ));

        // O(n)으로 처리
        learningWithSolving.forEach(learning -> {
            if (learning.getMode().equals(LearningMode.EXAM) && map.containsKey(ActivityType.EXAM)) {
                PlanProgress planProgress = map.get(ActivityType.EXAM);
                planProgress.addCompletedValue(1L);
                map.put(ActivityType.EXAM, planProgress);
            }

            if (learning.getMode().equals(LearningMode.STUDY) && map.containsKey(ActivityType.STUDY)) {
                PlanProgress planProgress = map.get(ActivityType.STUDY);
                planProgress.addCompletedValue(1L);
                map.put(ActivityType.STUDY, planProgress);
            }

            if (map.containsKey(ActivityType.PROBLEM)) {
                PlanProgress planProgress = map.get(ActivityType.PROBLEM);
                planProgress.addCompletedValue(learning.getSolvingCount());
            }

            if (map.containsKey(ActivityType.TIME)) {
                PlanProgress planProgress = map.get(ActivityType.TIME);
                planProgress.addCompletedValue(learning.getLearningTime());
            }
        });

        return map.values().stream().toList();
    }
}
