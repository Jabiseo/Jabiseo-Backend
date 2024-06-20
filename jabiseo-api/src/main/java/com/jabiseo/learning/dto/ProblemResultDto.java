package com.jabiseo.learning.dto;

public record ProblemResultDto(
        String problemId,
        int choiceNumber,
        boolean isCorrect
) {
}
