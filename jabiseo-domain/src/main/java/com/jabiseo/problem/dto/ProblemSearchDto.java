package com.jabiseo.problem.dto;

public record ProblemSearchDto(
        Long problemId,
        Double score
) {
    public static ProblemSearchDto of(Long problemId, Double score) {
        return new ProblemSearchDto(problemId, score);
    }
}
