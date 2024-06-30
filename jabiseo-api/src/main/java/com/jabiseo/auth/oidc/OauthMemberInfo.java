package com.jabiseo.auth.oidc;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthMemberInfo {

    private  String oauthId;
    private String oauthServer;


    @Builder
    public OauthMemberInfo(String oauthId, String oauthServer) {
        this.oauthId = oauthId;
        this.oauthServer = oauthServer;
    }
}
