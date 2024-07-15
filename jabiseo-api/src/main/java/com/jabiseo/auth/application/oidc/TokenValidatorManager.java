package com.jabiseo.auth.application.oidc;


import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import com.jabiseo.member.domain.OauthServer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class TokenValidatorManager {

    private final Map<OauthServer, AbstractIdTokenValidator> validatorMap = new HashMap<>();

    public TokenValidatorManager(Set<AbstractIdTokenValidator> idTokenValidators) {
        idTokenValidators.forEach((v) -> validatorMap.put(v.getOauthServer(), v));
    }

    public OauthMemberInfo validate(String idToken, OauthServer oauthServer) {
        AbstractIdTokenValidator idTokenValidator = validatorMap.get(oauthServer);

        if (idTokenValidator == null) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.NOT_SUPPORT_OAUTH);
        }

        return idTokenValidator.validate(idToken);
    }
}
