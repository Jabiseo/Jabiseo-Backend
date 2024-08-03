package com.jabiseo.problem.dto;

public record ProblemWithBookmarkDetailDto (
    Long problemId,
    String description,
    String choice1,
    String choice2,
    String choice3,
    String choice4,
    int answerNumber,
    String solution,
    boolean isBookmark,

    Long examId,
    String examDescription,
    int examYear,
    int yearRound,

    Long subjectId,
    String subjectName,
    int subjectSequence
) {
}
