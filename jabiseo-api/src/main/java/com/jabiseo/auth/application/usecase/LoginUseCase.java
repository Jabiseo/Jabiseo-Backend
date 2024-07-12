package com.jabiseo.auth.application.usecase;

import com.jabiseo.auth.application.MemberFactory;
import com.jabiseo.auth.dto.LoginRequest;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.auth.application.JwtHandler;
import com.jabiseo.auth.application.oidc.OauthMemberInfo;
import com.jabiseo.auth.application.oidc.TokenValidatorManager;
import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class LoginUseCase {

    private final TokenValidatorManager tokenValidatorManager;
    private final MemberFactory memberFactory;
    private final JwtHandler jwtHandler;
    private final MemberRepository memberRepository;
    private final RedisCacheRepository cacheRepository;

    public LoginResponse execute(LoginRequest loginRequest) {
        OauthMemberInfo oauthMemberInfo = tokenValidatorManager.validate(loginRequest.idToken(), loginRequest.oauthServer());

        Member member = memberRepository.findByOauthIdAndOauthServer(oauthMemberInfo.getOauthId(), oauthMemberInfo.getOauthServer())
                .orElse(null);

        if (isRequireSignup(member)) {
            Member newMember = memberFactory.createNew(oauthMemberInfo);
            member = memberRepository.save(newMember);
        }


        String accessToken = jwtHandler.createAccessToken(member);
        String refreshToken = jwtHandler.createRefreshToken();
        cacheRepository.saveToken(member.getId(), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }


    private boolean isRequireSignup(Member member) {
        return member == null;
    }
}
