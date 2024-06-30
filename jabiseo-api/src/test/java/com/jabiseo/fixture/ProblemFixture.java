package com.jabiseo.fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.problem.domain.Problem;

import static com.jabiseo.fixture.CertificateFixture.createCertificate;
import static com.jabiseo.fixture.ExamFixture.createExam;
import static com.jabiseo.fixture.SubjectFixture.createSubject;

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

    public static Problem createProblem(String id) {
        Certificate certificate = createCertificate("1234");
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
                createExam("5432", certificate),
                createSubject("9876", certificate)
        );
    }
}
