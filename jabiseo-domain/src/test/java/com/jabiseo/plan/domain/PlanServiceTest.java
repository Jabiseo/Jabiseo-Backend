package com.jabiseo.plan.domain;

import com.jabiseo.member.domain.Member;
import com.jabiseo.plan.exception.PlanBusinessException;
import fixture.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @InjectMocks
    private PlanService planService;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanItemRepository planItemRepository;

    @Test
    @DisplayName("이미 진행중인 플랜이 있는 경우 예외를 반환한다")
    void existInProgressPlanThrownException() {
        //given
        Member member = MemberFixture.createMember();

        given(planRepository.existsByCertificateAndMember(member.getCurrentCertificate(), member))
                .willReturn(true);
        //when then
        Assertions.assertThatThrownBy(()->planService.checkInProgressPlan(member))
                .isInstanceOf(PlanBusinessException.class);


    }

}