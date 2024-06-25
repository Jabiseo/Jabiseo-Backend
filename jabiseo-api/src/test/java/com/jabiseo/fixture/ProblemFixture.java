package com.jabiseo.fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.problem.domain.Problem;

public class ProblemFixture {
    public static Problem createProblem(String id, Certificate certificate, Exam exam, Subject subject) {
        return Problem.of(
                id,
                "problem description",
                "choice1",
                "choice2",
                "choice3",
                "choice4",
                "choice5",
                1,
                "problem theory",
                "problem solution",
                certificate,
                exam,
                subject
        );
    }
}
