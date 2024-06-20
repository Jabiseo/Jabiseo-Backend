package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.dto.ExamDto;
import com.jabiseo.certificate.dto.SubjectDto;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBookmarkedProblemsUseCase {

    public List<FindBookmarkedProblemsResponse> execute() {
        return List.of(
                new FindBookmarkedProblemsResponse(
                        "problemId",
                        new ExamDto(
                                "examId",
                                "description"
                        ),
                        new SubjectDto(
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
