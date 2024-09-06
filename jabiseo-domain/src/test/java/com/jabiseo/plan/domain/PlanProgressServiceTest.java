package com.jabiseo.plan.domain;

import com.jabiseo.learning.domain.LearningMode;
import com.jabiseo.learning.domain.LearningRepository;
import com.jabiseo.learning.dto.LearningWithSolvingCountQueryDto;
import com.jabiseo.member.domain.Member;
import fixture.MemberFixture;
import fixture.PlanItemFixture;
import fixture.PlanProgressFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fixture.PlanProgressFixture.createPlanProgress;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlanProgressServiceTest {

    @InjectMocks
    private PlanProgressService planProgressService;
    @Mock
    private PlanProgressRepository planProgressRepository;
    @Mock
    private LearningRepository learningRepository;

    @Mock
    private WeeklyDefineStrategy weeklyDefineStrategy;

    private Member member;
    private WeekPeriod weekPeriod;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember();
    }

    @Test
    @DisplayName("정상 동작 테스트, 주간  일간에 맞게 2번 호출한다")
    void successCallSaveAllTwo() {
        //given
        List<PlanItem> planItems = Arrays.asList(
                PlanItemFixture.createPlanItem(ActivityType.EXAM, GoalType.DAILY),
                PlanItemFixture.createPlanItem(ActivityType.STUDY, GoalType.DAILY),
                PlanItemFixture.createPlanItem(ActivityType.STUDY, GoalType.WEEKLY),
                PlanItemFixture.createPlanItem(ActivityType.PROBLEM, GoalType.WEEKLY)
        );
        List<LearningWithSolvingCountQueryDto> queryDtos = Arrays.asList(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 10L, LocalDateTime.now(), 10L));
        WeekPeriod currentWeekPeriod = new WeekPeriod(LocalDate.now().withDayOfMonth(1), LocalDate.now().plusDays(6));

        given(weeklyDefineStrategy.getCurrentWeekPeriod(LocalDate.now())).willReturn(currentWeekPeriod);
        given(learningRepository.findLearningWithSolvingCount(member, member.getCurrentCertificate(),
                LocalDate.now(), LocalDate.now())).willReturn(queryDtos);
        given(learningRepository.findLearningWithSolvingCount(member, member.getCurrentCertificate(),
                currentWeekPeriod.getStart(), currentWeekPeriod.getEnd())).willReturn(queryDtos);

        //when
        planProgressService.createCurrentPlanProgress(member, planItems);

        //then
        verify(planProgressRepository, times(2)).saveAll(any());
    }

    @Test
    @DisplayName("없는 goalType에 대해 DB 로직 호출을 하지 않는다. ")
    void callSaveAllOne() {
        //given
        List<PlanItem> planItems = Arrays.asList(
                PlanItemFixture.createPlanItem(ActivityType.EXAM, GoalType.DAILY),
                PlanItemFixture.createPlanItem(ActivityType.STUDY, GoalType.DAILY)
        );
        List<LearningWithSolvingCountQueryDto> queryDtos = Arrays.asList(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 10L, LocalDateTime.now(), 10L));

        given(weeklyDefineStrategy.getCurrentWeekPeriod(LocalDate.now())).willReturn(new WeekPeriod(LocalDate.now(), LocalDate.now().plusDays(1)));
        given(learningRepository.findLearningWithSolvingCount(member, member.getCurrentCertificate(),
                LocalDate.now(), LocalDate.now())).willReturn(queryDtos);

        //when
        planProgressService.createCurrentPlanProgress(member, planItems);

        //then
        verify(planProgressRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("calculateProgress 메소드 progress 계산 로직 테스트")
    void calculateProgressTest() {
        //given
        List<PlanProgress> planProgresses = Arrays.asList(
                createPlanProgress(LocalDate.now(), GoalType.DAILY, ActivityType.EXAM),
                createPlanProgress(LocalDate.now(), GoalType.DAILY, ActivityType.STUDY),
                createPlanProgress(LocalDate.now(), GoalType.DAILY, ActivityType.PROBLEM),
                createPlanProgress(LocalDate.now(), GoalType.DAILY, ActivityType.TIME)
        );
        List<LearningWithSolvingCountQueryDto> datas = dummyDatas();


        //when
        List<PlanProgress> result = planProgressService.calculateProgress(planProgresses, datas);

        //then
        result.forEach((planProgress -> {
            long expectedValue = -1;
            if (planProgress.getActivityType().equals(ActivityType.EXAM)) {
                expectedValue = datas.stream()
                        .filter(pi -> pi.getMode().equals(LearningMode.EXAM))
                        .count();
            }
            if (planProgress.getActivityType().equals(ActivityType.STUDY)) {
                expectedValue = datas.stream()
                        .filter(pi -> pi.getMode().equals(LearningMode.STUDY))
                        .count();
            }
            if (planProgress.getActivityType().equals(ActivityType.PROBLEM)) {
                expectedValue = datas.stream()
                        .mapToLong(LearningWithSolvingCountQueryDto::getSolvingCount).sum();
            }
            if (planProgress.getActivityType().equals(ActivityType.TIME)) {
                expectedValue = datas.stream()
                        .mapToLong(LearningWithSolvingCountQueryDto::getLearningTime).sum();
            }

            Assertions.assertThat(planProgress.getCompletedValue()).isEqualTo(expectedValue);
        }));

    }

    private List<LearningWithSolvingCountQueryDto> dummyDatas() {
        List<LearningWithSolvingCountQueryDto> datas = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 5000L, now, 1L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 5000L, now, 2L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 5000L, now, 3L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 5000L, now, 4L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.STUDY, 5000L, now, 5L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.STUDY, 5000L, now, 6L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.STUDY, 5000L, now, 7L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.STUDY, 5000L, now, 8L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.STUDY, 5000L, now, 9L));
        datas.add(new LearningWithSolvingCountQueryDto(LearningMode.EXAM, 5000L, now, 10L));
        return datas;
    }
}