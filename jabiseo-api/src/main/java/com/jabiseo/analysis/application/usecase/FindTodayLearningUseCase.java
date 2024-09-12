package com.jabiseo.analysis.application.usecase;

import com.jabiseo.analysis.dto.FindTodayLearningResponse;
import com.jabiseo.learning.dto.TodayLearningDto;
import com.jabiseo.learning.service.LearningService;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTodayLearningUseCase {

    private final MemberRepository memberRepository;
    private final LearningService learningService;

    public FindTodayLearningResponse execute(Long memberId) {

        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();

        TodayLearningDto todayLearningDto = learningService.findTodayLearning(member);

        return FindTodayLearningResponse.from(todayLearningDto);
    }

}
