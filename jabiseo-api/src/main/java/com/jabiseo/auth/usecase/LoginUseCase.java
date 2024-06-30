package com.jabiseo.auth.usecase;

import com.jabiseo.auth.dto.LoginRequest;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.auth.oidc.OauthMemberInfo;
import com.jabiseo.auth.oidc.TokenValidatorManager;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberFactory;
import com.jabiseo.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final TokenValidatorManager tokenValidatorManager;
    private final MemberFactory memberFactory;
    private final MemberRepository memberRepository;

    public LoginResponse execute(LoginRequest loginRequest) {
        OauthMemberInfo oauthMemberInfo = tokenValidatorManager.validate(loginRequest.idToken(), loginRequest.oauthServer());

        String oauthId = oauthMemberInfo.getOauthId();
        String oauthServer = oauthMemberInfo.getOauthServer();

        Member member = memberRepository.findByOauthIdAndOauthServer(oauthId, oauthServer)
                .orElse(null);

        if (isRequireSignup(member)) {
            Member newMember = memberFactory.createNew(oauthId, oauthServer);
            member = memberRepository.save(newMember);
        }



        return new LoginResponse("access_token", "refresh_token");
    }


    private static boolean isRequireSignup(Member member) {
        return member == null;
    }
}
