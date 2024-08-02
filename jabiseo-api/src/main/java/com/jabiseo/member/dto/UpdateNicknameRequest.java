package com.jabiseo.member.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequest(@NotBlank String nickname) {
}
