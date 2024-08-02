package com.jabiseo.auth.dto;

import com.jabiseo.common.validator.EnumValid;
import com.jabiseo.member.domain.OauthServer;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String idToken,
        @EnumValid(enumClass = OauthServer.class, message = "oauthServer Type이 잘못됐습니다.")
        String oauthServer) {
}
