package fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;

public class ExamFixture {

    public static Exam createExam(Long examId, Certificate certificate) {
        return Exam.of(examId, "exam description", 2000, 1, certificate);
    }

}
