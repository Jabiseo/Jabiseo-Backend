package com.jabiseo.plan.domain;


import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.LearningMode;
import com.jabiseo.learning.domain.LearningRepository;
import com.jabiseo.learning.dto.LearningWithSolvingCountQueryDto;
import com.jabiseo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanProgressService {

    private final WeeklyDefineStrategy weeklyDefineStrategy;
    private final PlanRepository planRepository;
    private final PlanProgressRepository planProgressRepository;
    private final LearningRepository learningRepository;

    public List<PlanProgress> findByYearMonth(Plan plan, int year, int month) {
        List<WeekPeriod> periodPerWeek = weeklyDefineStrategy.getPeriodPerWeek(year, month);
        LocalDate startQueryDate = periodPerWeek.get(0).getStart();
        LocalDate endQueryDate = periodPerWeek.get(periodPerWeek.size() - 1).getEnd();

        return planProgressRepository.findAllByPlanAndProgressDateBetweenOrderByProgressDate(plan, startQueryDate, endQueryDate);
    }

    public void updateProgress(Learning learning, long count) {
        Member member = learning.getMember();
        Optional<Plan> plans = planRepository.findFirstByCertificateAndMember(member.getCurrentCertificate(), member);

        if (plans.isEmpty()) {
            return;
        }

        List<PlanItem> planItems = plans.get().getPlanItems();
        List<PlanProgress> planProgress = getPlanProgress(planItems, LearningWithSolvingCountQueryDto.from(learning, count));
        planProgressRepository.saveAll(planProgress);
    }


    // 플랜 생성 시, 설정된 플랜 기간 중에 학습한 기록이 있다면 반영한다
    public void createCurrentPlanProgress(Member member, List<PlanItem> planItems) {
        WeekPeriod currentWeekPeriod = weeklyDefineStrategy.getWeekPeriod(LocalDate.now());


        List<PlanProgress> daily = itemToDailyPlanProgress(planItems);

        List<PlanProgress> weekly = itemToWeeklyPlanProgress(planItems, currentWeekPeriod.getEnd());

        learningCalculateAndSave(member, daily, LocalDate.now(), LocalDate.now());
        learningCalculateAndSave(member, weekly, currentWeekPeriod.getStart(), currentWeekPeriod.getEnd());
    }

    private List<PlanProgress> getPlanProgress(List<PlanItem> planItems, LearningWithSolvingCountQueryDto dto) {
        WeekPeriod currentWeekPeriod = weeklyDefineStrategy.getWeekPeriod(LocalDate.now());

        List<PlanProgress> dailyProgress = planProgressRepository.findAllByProgressDateBetweenAndGoalType(LocalDate.now(), LocalDate.now(), GoalType.DAILY);
        if (dailyProgress.isEmpty()) {
            dailyProgress = itemToDailyPlanProgress(planItems);
        }

        List<PlanProgress> weeklyProgress = planProgressRepository.findAllByProgressDateBetweenAndGoalType(currentWeekPeriod.getStart(), currentWeekPeriod.getEnd(), GoalType.WEEKLY);
        if (weeklyProgress.isEmpty()) {
            weeklyProgress = itemToWeeklyPlanProgress(planItems, currentWeekPeriod.getEnd());
        }

        List<PlanProgress> result = new ArrayList<>();
        result.addAll(calculateProgress(dailyProgress, List.of(dto)));
        result.addAll(calculateProgress(weeklyProgress, List.of(dto)));
        return result;
    }


    private List<PlanProgress> itemToDailyPlanProgress(List<PlanItem> planItems) {
        return planItems.stream()
                .filter(p -> p.getGoalType().equals(GoalType.DAILY))
                .map(planItem -> planItem.toPlanProgress(LocalDate.now()))
                .toList();
    }

    private List<PlanProgress> itemToWeeklyPlanProgress(List<PlanItem> planItems, LocalDate date) {
        return planItems.stream()
                .filter(p -> p.getGoalType().equals(GoalType.WEEKLY))
                .map(planItem -> planItem.toPlanProgress(date))
                .toList();
    }

    private void learningCalculateAndSave(Member member, List<PlanProgress> progress, LocalDate start, LocalDate end) {
        // PlanProgress 로 전환할 PlanItem 이 없는 경우 PlanProgress 를 생성하지 않는다
        if (progress.isEmpty()) {
            return;
        }

        List<LearningWithSolvingCountQueryDto> queryResult = learningRepository.findLearningWithSolvingCount(member, member.getCurrentCertificate(), start, end);
        // 해당하는 기간(start, end) 에 학습를 하지 않았으면 PlanProgress 를 생성하지 않는다
        if (queryResult.isEmpty()) {
            return;
        }

        // 플랜 생성 전, 해당 기간에 학습을 진행했으므로 PlanProgress 를 생성한다
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
            }

            if (learning.getMode().equals(LearningMode.STUDY) && map.containsKey(ActivityType.STUDY)) {
                PlanProgress planProgress = map.get(ActivityType.STUDY);
                planProgress.addCompletedValue(1L);
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
