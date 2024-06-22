package com.jabiseo.learning.dto;

public record ProblemResultRequest(
        String problemId,
        int choiceNumber,
        boolean isCorrect
) {
}
