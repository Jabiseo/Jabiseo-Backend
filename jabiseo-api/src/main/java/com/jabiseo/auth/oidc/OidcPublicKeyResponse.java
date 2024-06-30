package com.jabiseo.auth.oidc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
class OidcPublicKeyResponse {

    private List<OidcPublicKey> keys;
}
