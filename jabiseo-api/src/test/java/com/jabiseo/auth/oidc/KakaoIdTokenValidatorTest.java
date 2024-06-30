package com.jabiseo.auth.oidc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class KakaoIdTokenValidatorTest {

    @Autowired
    KakaoIdTokenValidator validator;

    @Test
    void valid() {

        validator.getOidcPublicKey("kids..");
    }

}
