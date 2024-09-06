package com.jabiseo.plan.application.usecase;

import com.jabiseo.plan.domain.*;
import com.jabiseo.plan.dto.calender.PlanCalenderSearchResponse;
import com.jabiseo.plan.dto.calender.PlanProgressDateResponse;
import com.jabiseo.plan.dto.calender.PlanProgressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanCalenderSearchUseCase {

    private final PlanProgressService planProgressService;
    private final PlanRepository planRepository;
    private final WeeklyDefineStrategy weeklyDefineStrategy;

    public PlanCalenderSearchResponse execute(Long planId, int year, int month) {
        Plan plan = planRepository.getReferenceById(planId);
        List<PlanProgress> progress = planProgressService.findByYearMonth(plan, year, month);

        List<WeekPeriod> periodPerWeek = weeklyDefineStrategy.getPeriodPerWeek(year, month);
        List<PlanProgressDateResponse> daily = dailyProgressCovert(progress);
        List<PlanProgressDateResponse> weekly = weeklyProgressCovert(progress, periodPerWeek);

        return new PlanCalenderSearchResponse(year, month, daily, weekly);
    }

    private List<PlanProgressDateResponse> weeklyProgressCovert(List<PlanProgress> progressList, List<WeekPeriod> weekPeriods) {
        Map<LocalDate, List<PlanProgress>> map = progressList.stream().filter(planProgress -> planProgress.getGoalType().equals(GoalType.WEEKLY))
                .collect(Collectors.groupingBy(PlanProgress::getProgressDate, Collectors.toList()));

        return map.entrySet().stream()
                .sorted()
                .map(entry -> {
                    List<PlanProgressResponse> list = entry.getValue()
                            .stream()
                            .map((v) -> new PlanProgressResponse(v.getActivityType(), v.getCompletedValue(), v.getTargetValue()))
                            .toList();
                    return PlanProgressDateResponse.ofWeek(findIndexWeek(weekPeriods, entry.getKey()) + 1, list);
                }).toList();
    }

    private static int findIndexWeek(List<WeekPeriod> weekPeriods, LocalDate date) {
        OptionalInt index = IntStream.range(0, weekPeriods.size())
                .filter(i -> {
                    WeekPeriod period = weekPeriods.get(i);
                    return !date.isBefore(period.getStart()) && !date.isAfter(period.getEnd());
                })
                .findFirst();

        return index.orElse(-1);
    }

    private List<PlanProgressDateResponse> dailyProgressCovert(List<PlanProgress> progressList) {
        Map<LocalDate, List<PlanProgress>> map = progressList.stream().filter(planProgress -> planProgress.getGoalType().equals(GoalType.DAILY))
                .collect(Collectors.groupingBy(PlanProgress::getProgressDate, Collectors.toList()));

        return map.entrySet().stream()
                .sorted()
                .map(entry -> {
                    List<PlanProgressResponse> list = entry.getValue()
                            .stream()
                            .map((v) -> new PlanProgressResponse(v.getActivityType(), v.getCompletedValue(), v.getTargetValue()))
                            .toList();
                    return PlanProgressDateResponse.ofDay(entry.getKey(), list);
                }).toList();
    }


}
