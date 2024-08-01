package com.jabiseo.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemWithBookmarkDto {
    private Long problemId;
    private String description;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int answerNumber;
    private String solution;
    private boolean isBookmark;

    private Long examId;
    private String examDescription;
    private int examYear;
    private int yearRound;

    private Long subjectId;
    private String subjectName;
    private int subjectSequence;
}
