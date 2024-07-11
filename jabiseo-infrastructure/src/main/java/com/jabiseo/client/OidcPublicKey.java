package com.jabiseo.client;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class OidcPublicKey {

    private String kid;
    private String alg;
    private String use;
    private String n;
    private String e;

    public OidcPublicKey(String kid, String alg, String use, String n, String e) {
        this.kid = kid;
        this.alg = alg;
        this.use = use;
        this.n = n;
        this.e = e;
    }
}
