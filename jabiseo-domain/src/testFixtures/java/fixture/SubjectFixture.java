package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Subject;

public class SubjectFixture {

    public static Subject createSubject(Long subjectId, Certificate certificate) {
        return Subject.of(subjectId, "subject name", 1, certificate);
    }

}
