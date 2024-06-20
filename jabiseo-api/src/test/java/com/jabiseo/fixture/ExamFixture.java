package com.jabiseo.fixture;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;

public class ExamFixture {

    public static Exam createExam(String examId, Certificate certificate) {
        return Exam.of(examId, "exam description", certificate);
    }

}
