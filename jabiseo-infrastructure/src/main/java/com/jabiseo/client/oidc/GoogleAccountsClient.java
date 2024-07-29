package com.jabiseo.client.oidc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface GoogleAccountsClient {

    @GetExchange(url = "/.well-known/openid-configuration")
    ResponseEntity<GoogleOpenIdConfiguration> getOidcConfiguration();

}
