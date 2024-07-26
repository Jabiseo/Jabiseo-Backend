package com.jabiseo.member.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.FindMyCertificateStateResponse;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindMyCertificateStateUseCase {

    private final MemberRepository memberRepository;

    public FindMyCertificateStateResponse execute(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (member.getCertificateState() == null) {
            return FindMyCertificateStateResponse.from(member);
        } else {
            return FindMyCertificateStateResponse.of(member, member.getCertificateState());
        }
    }
}
