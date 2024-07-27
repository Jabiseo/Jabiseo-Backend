package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.problem.domain.Problem;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.SubjectFixture.createSubject;

public class ProblemFixture {
    public static Problem createProblem(Long id, Certificate certificate, Exam exam, Subject subject) {
        return Problem.of(
                id,
                "problem description",
                "choice1",
                "choice2",
                "choice3",
                "choice4",
                1,
                "problem theory",
                1,
                certificate,
                exam,
                subject
        );
    }
    public static Problem createProblem(Long id, Certificate certificate) {
        return Problem.of(
                id,
                "problem description",
                "choice1",
                "choice2",
                "choice3",
                "choice4",
                1,
                "problem theory",
                1,
                certificate,
                createExam(5432L, certificate),
                createSubject(9876L, certificate)
        );
    }


    public static Problem createProblem(Long id) {
        Certificate certificate = createCertificate(1234L);
        return Problem.of(
                id,
                "problem description",
                "choice1",
                "choice2",
                "choice3",
                "choice4",
                1,
                "problem theory",
                1,
                certificate,
                createExam(5432L, certificate),
                createSubject(9876L, certificate)
        );
    }
}
