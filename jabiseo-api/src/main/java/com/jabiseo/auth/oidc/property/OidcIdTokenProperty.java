package com.jabiseo.auth.oidc.property;


/**
 * @param issuer   발행 회사 url
 * @param audience client-id
 */
public record OidcIdTokenProperty(String issuer, String audience) {

}
