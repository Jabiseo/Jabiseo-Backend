package com.jabiseo.member.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.FindMyCertificateStateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindMyCertificateStateUseCase {

    private final MemberRepository memberRepository;

    public FindMyCertificateStateResponse execute(String memberId) {
        Member member = memberRepository.findById(memberId);
        return FindMyCertificateStateResponse.of(member, member.getCertificateState());
    }
}
