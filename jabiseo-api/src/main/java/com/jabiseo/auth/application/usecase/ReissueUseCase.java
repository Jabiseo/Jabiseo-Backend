package com.jabiseo.auth.application.usecase;

import com.jabiseo.auth.application.JwtHandler;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.auth.dto.ReissueRequest;
import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueUseCase {

    /*
     *
     */

    private final MemberRepository memberRepository;
    private final RedisCacheRepository redisCacheRepository;
    private final JwtHandler jwtHandler;

    public LoginResponse execute(ReissueRequest request, String memberId) {
        Member member = memberRepository.getReferenceById(memberId);

        String token = redisCacheRepository.getToken(memberId);
        if (!isCorretToken(token, request.refreshToken())) {
        }
        return new LoginResponse("access_token", "refresh_token");
    }

    private boolean isCorretToken(String savedToken, String request) {
        return request.equals(savedToken);
    }


}
