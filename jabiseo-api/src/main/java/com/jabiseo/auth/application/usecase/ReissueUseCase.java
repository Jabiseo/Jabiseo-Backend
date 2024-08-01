package com.jabiseo.auth.application.usecase;

import com.jabiseo.auth.application.JwtHandler;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.auth.dto.ReissueRequest;
import com.jabiseo.auth.dto.ReissueResponse;
import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReissueUseCase {

    private final MemberRepository memberRepository;
    private final RedisCacheRepository redisCacheRepository;
    private final JwtHandler jwtHandler;

    public ReissueResponse execute(ReissueRequest request, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        jwtHandler.validateRefreshToken(request.refreshToken());

        String savedToken = redisCacheRepository.findToken(memberId)
                .orElseThrow(() -> new AuthenticationBusinessException(AuthenticationErrorCode.REQUIRE_LOGIN));

        if (!savedToken.equals(request.refreshToken())) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.NOT_MATCH_REFRESH);
        }

        String accessToken = jwtHandler.createAccessToken(member);
        return new ReissueResponse(accessToken);
    }


}
