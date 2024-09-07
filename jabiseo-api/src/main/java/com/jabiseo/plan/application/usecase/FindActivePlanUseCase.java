package com.jabiseo.plan.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.plan.domain.GoalType;
import com.jabiseo.plan.domain.Plan;
import com.jabiseo.plan.domain.PlanItem;
import com.jabiseo.plan.domain.PlanRepository;
import com.jabiseo.plan.dto.ActivePlanResponse;
import com.jabiseo.plan.dto.PlanItemResponse;
import com.jabiseo.plan.exception.PlanBusinessException;
import com.jabiseo.plan.exception.PlanErrorCode;
import com.jabiseo.problem.dto.CertificateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class FindActivePlanUseCase {

    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    public ActivePlanResponse execute(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();

        Plan plan = planRepository.findFirstByCertificateAndMember(member.getCurrentCertificate(), member)
                .orElseThrow(() -> new PlanBusinessException(PlanErrorCode.NOT_FOUND_PLAN));

        List<PlanItem> planItems = plan.getPlanItems();

        return ActivePlanResponse.builder()
                .planId(String.valueOf(plan.getId()))
                .certificate(CertificateResponse.from(plan.getCertificate()))
                .endAt(plan.getEndAt())
                .createdAt(plan.getCreatedAt().toLocalDate())
                .weeklyPlanItems(planItems.stream().filter((p -> p.getGoalType().equals(GoalType.WEEKLY))).map(PlanItemResponse::from).toList())
                .dailyPlanItems(planItems.stream().filter((p -> p.getGoalType().equals(GoalType.DAILY))).map(PlanItemResponse::from).toList())
                .build();
    }

}
