package com.jabiseo.member.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.FindMyCertificateStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindMyCertificateStatusUseCase {

    private final MemberRepository memberRepository;

    public FindMyCertificateStatusResponse execute(String memberId) {
        Member member = memberRepository.findById(memberId);
        return FindMyCertificateStatusResponse.from(member);
    }
}
