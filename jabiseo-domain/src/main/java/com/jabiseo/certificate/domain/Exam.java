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
    private String id;

    private String description;

    @Column(name = "exam_year")
    private int year;

    private int round;

    @ManyToOne
    @JoinColumn(name = "certificate_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    private Exam(String id, String description, int year, int round, Certificate certificate) {
        this.id = id;
        this.description = description;
        this.year = year;
        this.round = round;
        this.certificate = certificate;
    }

    public static Exam of(String id, String description, int year, int round, Certificate certificate) {
        Exam exam = new Exam(id, description, year, round, certificate);
        certificate.addExam(exam);
        return exam;
    }

}
