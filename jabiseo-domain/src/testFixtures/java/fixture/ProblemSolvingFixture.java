package fixture;

import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.domain.Problem;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static fixture.CertificateFixture.createCertificate;
import static fixture.LearningFixture.createLearning;

public class ProblemSolvingFixture {

    public static ProblemSolving createProblemSolving(Long id, Member member, Problem problem) throws Exception {
        ProblemSolving problemSolving = ProblemSolving.of(
                member,
                problem,
                createLearning(12345L, createCertificate(), LocalDateTime.now()),
                4,
                true
        );
        ReflectionTestUtils.setField(problemSolving, "id", id);
        return problemSolving;
    }
}
