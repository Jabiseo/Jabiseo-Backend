package com.jabiseo.plan.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.plan.domain.*;
import com.jabiseo.plan.dto.CreatePlanRequest;
import com.jabiseo.plan.exception.PlanBusinessException;
import com.jabiseo.plan.exception.PlanErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatePlanUseCase {

    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;

    public Long execute(Long memberId, CreatePlanRequest request) {
        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();

        if (isInProgressPlanExists(member)) {
            throw new PlanBusinessException(PlanErrorCode.ALREADY_EXIST_PLAN);
        }

        Plan plan = Plan.create(member, request.endDay());
        List<PlanItem> planItems = generatePlanItems(request, plan);
        planRepository.save(plan);
        planItemRepository.saveAll(planItems);
        return plan.getId();
    }

    private List<PlanItem> generatePlanItems(CreatePlanRequest request, Plan plan) {
        List<PlanItem> planItems = new ArrayList<>();
        request.dailyPlan().forEach((p) -> planItems.add(new PlanItem(plan, ActivityType.valueOf(p.activityType()), GoalType.DAILY, p.targetValue())));
        request.weeklyPlan().forEach((p) -> planItems.add(new PlanItem(plan, ActivityType.valueOf(p.activityType()), GoalType.WEEKLY, p.targetValue())));
        return planItems;
    }

    private boolean isInProgressPlanExists(Member member) {
        return planRepository.existsByCertificateAndMemberAndEndAtAfter(member.getCurrentCertificate(), member, LocalDate.now());
    }

}
