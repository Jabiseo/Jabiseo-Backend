package com.jabiseo.problem.dto;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.domain.Problem;

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
    public static ProblemsDetailResponse fromNonLogin(Problem problem) {
        return new ProblemsDetailResponse(
                problem.getId(),
                ExamResponse.from(problem.getExam()),
                SubjectResponse.from(problem.getSubject()),
                false,
                problem.getDescription(),
                ChoiceResponse.fromChoices(problem.getChoices()),
                problem.getAnswerNumber(),
                problem.getSolution()
        );
    }

    public static ProblemsDetailResponse from(ProblemWithBookmarkDto bookmarkedProblemDto) {
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
