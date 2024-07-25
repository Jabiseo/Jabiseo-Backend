package com.jabiseo.member.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.FindMyInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMyInfoUseCase {

    private final MemberRepository memberRepository;

    public FindMyInfoResponse execute(String id) {
        Member member = memberRepository.getReferenceById(id);
        return FindMyInfoResponse.from(member);
    }
}
