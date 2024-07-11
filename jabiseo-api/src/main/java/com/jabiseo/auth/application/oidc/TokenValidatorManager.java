package com.jabiseo.auth.application.oidc;


import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidatorManager {

    private final AbstractIdTokenValidator kakaoIdTokenValidator;

    public OauthMemberInfo validate(String idToken, String oauthServer) {
        if (oauthServer.equals("KAKAO")) {
            return kakaoIdTokenValidator.validate(idToken);
        } else {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.NOT_SUPPORT_OAUTH);
        }
    }
}
