package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBookmarkedProblemsUseCase {

    public List<FindBookmarkedProblemsResponse> execute() {
        return List.of(
                new FindBookmarkedProblemsResponse(
                        "problemId",
                        new ExamResponse(
                                "examId",
                                "description"
                        ),
                        new SubjectResponse(
                                "subjectId",
                                3,
                                "name"
                        ),
                        true,
                        "description"
                )
        );
    }
}
