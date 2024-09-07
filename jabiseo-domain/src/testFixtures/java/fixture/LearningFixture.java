package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.LearningMode;
import com.jabiseo.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class LearningFixture {

    public static Learning createLearning(Long id, Certificate certificate) {
        Learning learning = Learning.of(LearningMode.EXAM, 123L, certificate, MemberFixture.createMember());
        ReflectionTestUtils.setField(learning, "id", id);
        return learning;
    }

}
