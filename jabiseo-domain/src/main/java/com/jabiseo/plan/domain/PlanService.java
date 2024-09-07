package com.jabiseo.plan.domain;

import com.jabiseo.member.domain.Member;
import com.jabiseo.plan.exception.PlanBusinessException;
import com.jabiseo.plan.exception.PlanErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;

    public Plan savePlanAndItems(Plan plan, List<PlanItem> planItems) {
        planItemRepository.saveAll(planItems);
        return planRepository.save(plan);
    }


    public void checkInProgressPlan(Member member, LocalDate now){
        if(planRepository.existsByCertificateAndMemberAndEndAtAfter(member.getCurrentCertificate(),member, now)){
            throw new PlanBusinessException(PlanErrorCode.ALREADY_EXIST_PLAN);
        }
    }

}
