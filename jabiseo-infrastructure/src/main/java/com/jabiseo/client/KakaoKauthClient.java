package com.jabiseo.client;

;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;



@HttpExchange
public interface KakaoKauthClient {

    @GetExchange(url = "/.well-known/jwks.json")
    ResponseEntity<OidcPublicKeyResponse> getPublicKeys();

}
