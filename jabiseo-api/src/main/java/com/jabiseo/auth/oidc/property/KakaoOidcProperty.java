package com.jabiseo.auth.oidc.property;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oidc.kakao")
public class KakaoOidcProperty {

    private final String clientId;
    private final String adminKey;
    private final String issuer;

    public KakaoOidcProperty(String clientId, String adminKey, String issuer) {
        this.clientId = clientId;
        this.adminKey = adminKey;
        this.issuer = issuer;
    }

    public OidcIdTokenProperty toIdTokenPropety() {
        return new OidcIdTokenProperty(issuer, clientId);
    }
}
