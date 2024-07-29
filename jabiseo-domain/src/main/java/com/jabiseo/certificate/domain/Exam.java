package com.jabiseo.certificate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exam {

    @Id
    @Column(name = "exam_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private int examYear;

    private int yearRound;

    @ManyToOne
    @JoinColumn(name = "certificate_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    private Exam(Long id, String description, int examYear, int yearRound, Certificate certificate) {
        this.id = id;
        this.description = description;
        this.examYear = examYear;
        this.yearRound = yearRound;
        this.certificate = certificate;
    }

    public static Exam of(Long id, String description, int examYear, int round, Certificate certificate) {
        Exam exam = new Exam(id, description, examYear, round, certificate);
        certificate.addExam(exam);
        return exam;
    }

}
