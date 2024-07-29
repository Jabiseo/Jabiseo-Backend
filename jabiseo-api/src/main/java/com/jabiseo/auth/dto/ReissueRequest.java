package com.jabiseo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
        @NotBlank
        String refreshToken
) {
}
