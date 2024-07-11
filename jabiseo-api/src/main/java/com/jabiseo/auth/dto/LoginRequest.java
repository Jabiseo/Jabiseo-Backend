package com.jabiseo.auth.dto;

import com.jabiseo.member.domain.OauthServer;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String idToken, @NotNull OauthServer oauthServer) {
}
