package com.jabiseo.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String idToken, @NotNull String oauthServer) {
}
