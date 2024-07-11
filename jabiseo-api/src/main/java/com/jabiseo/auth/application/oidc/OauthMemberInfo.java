package com.jabiseo.auth.application.oidc;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthMemberInfo {

    private final String oauthId;
    private final String oauthServer;


    @Builder
    public OauthMemberInfo(String oauthId, String oauthServer) {
        this.oauthId = oauthId;
        this.oauthServer = oauthServer;
    }
}
