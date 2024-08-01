package com.jabiseo.auth.application;


import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DevLoginHelper {

    private final JwtHandler jwtHandler;
    private final MemberRepository memberRepository;
    private final RedisCacheRepository redisCacheRepository;

    public LoginResponse login(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        String accessToken = jwtHandler.createAccessToken(member);
        String refreshToken = jwtHandler.createRefreshToken();
        redisCacheRepository.saveToken(member.getId(), refreshToken);
        return new LoginResponse(accessToken, refreshToken);
    }

}
