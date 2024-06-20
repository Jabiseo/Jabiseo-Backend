package com.jabiseo.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
