package com.jabiseo.learning.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ProblemResultRequest(
        Long problemId,
        @Min(value = 1L, message = "선지는 1번부터 4번까지 존재합니다.")
        @Max(value = 4L, message = "선지는 1번부터 4번까지 존재합니다.")
        int choice
) {
}
