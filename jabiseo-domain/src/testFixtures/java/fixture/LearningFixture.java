package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.LearningMode;
import com.jabiseo.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class LearningFixture {

    public static Learning createLearning(Long id, Member member, Certificate certificate) {
        Learning learning = Learning.of(LearningMode.EXAM, 123L, member, certificate);
        ReflectionTestUtils.setField(learning, "id", id);
        return learning;
    }

    public static Learning createLearning(Long id, Certificate certificate) {
        Learning learning = Learning.of(LearningMode.EXAM, 123L, MemberFixture.createMember(), certificate);
        ReflectionTestUtils.setField(learning, "id", id);
        return learning;
    }

    public static Learning createLearning(Long id, Certificate certificate, LocalDateTime createdAt) throws Exception {
        Learning learning = Learning.of(LearningMode.EXAM, 123L, MemberFixture.createMember(), certificate);
        ReflectionTestUtils.setField(learning, "id", id);
        ReflectionTestUtils.setField(learning, "createdAt", createdAt);
        return learning;
    }

}
