package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.dto.ExamDto;
import com.jabiseo.certificate.dto.SubjectDto;
import com.jabiseo.problem.dto.ChoiceDto;
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
                        new ExamDto("examId", "examDescription"),
                        new SubjectDto("subjectId", 3, "subjectName"),
                        true,
                        "description",
                        List.of(new ChoiceDto("choice")),
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
                        new ExamDto("examId", "examDescription"),
                        new SubjectDto("subjectId", 3, "subjectName"),
                        true,
                        "description",
                        List.of(new ChoiceDto("choice")),
                        1,
                        "theory",
                        "solution"
                )
        ));
    }
}
