package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.problem.domain.Problem;
import org.springframework.test.util.ReflectionTestUtils;

import static fixture.CertificateFixture.createCertificate;
import static fixture.SubjectFixture.createSubject;

public class ProblemFixture {
    public static Problem createProblem(Long id, Certificate certificate, Exam exam, Subject subject) {
        Problem problem = Problem.of(
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
        ReflectionTestUtils.setField(problem, "id", id);
        return problem;
    }

    public static Problem createProblem(Long id, Certificate certificate) {
        return createProblem(id, certificate, ExamFixture.createExam(5432L, certificate), createSubject(9876L, certificate));
    }


    public static Problem createProblem(Long id) {
        return createProblem(id, createCertificate(1234L));
    }

    public static Problem createProblem(Certificate certificate, Exam exam, Subject subject) {
        return Problem.of(
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
}
