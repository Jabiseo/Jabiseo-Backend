package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.problem.dto.ChoiceResponse;
import com.jabiseo.problem.dto.FindProblemsRequest;
import com.jabiseo.problem.dto.FindProblemsResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindProblemsUseCase {

    public List<FindProblemsResponse> execute(String certificateId, String subjectId, String examId, int count) {
        return new ArrayList<>(List.of(
                new FindProblemsResponse(
                        "1",
                        new ExamResponse("examId", "examDescription"),
                        new SubjectResponse("subjectId", 3, "subjectName"),
                        true,
                        "description",
                        List.of(new ChoiceResponse("choice")),
                        1,
                        "theory",
                        "solution"
                )
        ));
    }

    public List<FindProblemsResponse> execute(FindProblemsRequest request) {
        return new ArrayList<>(List.of(
                new FindProblemsResponse(
                        "1",
                        new ExamResponse("examId", "examDescription"),
                        new SubjectResponse("subjectId", 3, "subjectName"),
                        true,
                        "description",
                        List.of(new ChoiceResponse("choice")),
                        1,
                        "theory",
                        "solution"
                )
        ));
    }
}
