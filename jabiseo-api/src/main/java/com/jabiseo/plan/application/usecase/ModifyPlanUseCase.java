package com.jabiseo.plan.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.plan.domain.*;
import com.jabiseo.plan.dto.ModifyPlanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModifyPlanUseCase {

    private final PlanService planService;
    private final PlanProgressService planProgressService;
    private final MemberRepository memberRepository;

    public void execute(Long planId, Long memberId, ModifyPlanRequest request) {
        Plan plan = planService.getPlanWithItems(planId);
        Member member = memberRepository.getReferenceById(memberId);
        plan.checkOwner(memberId);

        plan.modifyEndDay(request.endAt());

        // 새로운 아이템은 추가한다.
        List<PlanItem> requestDailyPlanItems = request.getDailyPlanItems(plan);
        List<PlanItem> requestWeeklyPlanItems = request.getWeeklyPlanItems(plan);

        List<PlanItem> newItems = plan.getNewItems(requestDailyPlanItems, requestWeeklyPlanItems);
        planProgressService.createCurrentPlanProgress(member, newItems);

        List<PlanItem> existItems = plan.getExistItems(requestDailyPlanItems, requestWeeklyPlanItems);
        planProgressService.modifyCurrentPlanProgress(plan, filteringGoalType(existItems, GoalType.DAILY), filteringGoalType(existItems, GoalType.WEEKLY));

        List<PlanItem> deletedItems = plan.getDeletedItems(requestDailyPlanItems, requestWeeklyPlanItems);
        planProgressService.removeCurrentPlanProgress(plan, filteringGoalType(deletedItems, GoalType.DAILY), filteringGoalType(deletedItems, GoalType.WEEKLY));

        // Plan 객체의 정합성을 유지(DB 저장)
        plan.modifyPlanItems(requestDailyPlanItems, requestWeeklyPlanItems);
    }

    private List<PlanItem> filteringGoalType(List<PlanItem> planItems, GoalType goalType) {
        return planItems.stream()
                .filter((planItem) -> planItem.getGoalType().equals(goalType))
                .toList();
    }
}
