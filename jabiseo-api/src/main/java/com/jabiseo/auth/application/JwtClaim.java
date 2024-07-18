package com.jabiseo.auth.application;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class JwtClaim {

    private final String subject;
    private final HashMap<String, String> payloads;

    public JwtClaim(String subject) {
        this.subject = subject;
        this.payloads = new HashMap<>();
    }

    public JwtClaim(String subject, HashMap<String, String> payloads) {
        this.subject = subject;
        this.payloads = payloads;
    }

}
