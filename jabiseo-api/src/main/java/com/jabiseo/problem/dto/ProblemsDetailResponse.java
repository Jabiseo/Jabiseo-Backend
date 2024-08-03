package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;

import java.util.List;

public record ProblemsDetailResponse(
        Long problemId,
        ExamResponse examInfo,
        SubjectResponse subjectInfo,
        boolean isBookmark,
        String description,
        List<ChoiceResponse> choices,
        int answerNumber,
        String solution
) {
    public static ProblemsDetailResponse from(ProblemWithBookmarkDetailDto bookmarkedProblemDto) {
        return new ProblemsDetailResponse(
                bookmarkedProblemDto.getProblemId(),
                ExamResponse.of(
                        bookmarkedProblemDto.getExamId(),
                        bookmarkedProblemDto.getExamDescription()
                ),
                SubjectResponse.of(
                        bookmarkedProblemDto.getSubjectId(),
                        bookmarkedProblemDto.getSubjectSequence(),
                        bookmarkedProblemDto.getSubjectName()
                ),
                bookmarkedProblemDto.isBookmark(),
                bookmarkedProblemDto.getDescription(),
                ChoiceResponse.fromChoices(List.of(
                                bookmarkedProblemDto.getChoice1(),
                                bookmarkedProblemDto.getChoice2(),
                                bookmarkedProblemDto.getChoice3(),
                                bookmarkedProblemDto.getChoice4()
                        )
                ),
                bookmarkedProblemDto.getAnswerNumber(),
                bookmarkedProblemDto.getSolution()
        );
    }
}
