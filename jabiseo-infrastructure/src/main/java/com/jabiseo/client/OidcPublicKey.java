package com.jabiseo.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OidcPublicKey that = (OidcPublicKey) o;
        return Objects.equals(kid, that.kid);
    }

}
