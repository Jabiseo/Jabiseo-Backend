package com.jabiseo.member.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.UpdateNicknameRequest;
import com.jabiseo.member.dto.UpdateNicknameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateNicknameUseCase {

    private final MemberRepository memberRepository;

    public UpdateNicknameResponse updateNickname(Long memberId, UpdateNicknameRequest request) {
        Member member = memberRepository.getReferenceById(memberId);
        member.updateNickname(request.nickname());
        return UpdateNicknameResponse.of(member);
    }
}
