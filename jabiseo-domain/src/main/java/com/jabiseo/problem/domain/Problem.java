package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Problem {

    @Id
    @Column(name = "problem_id")
    private String id;

    private String description;

    private String choice1;

    private String choice2;

    private String choice3;

    private String choice4;

    private String choice5;

    private int answerNumber;

    private String theory;

    private String solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.EAGER) // TODO: 문제 조회 시 항상 필요해서 일단 EAGER로 설정. 논의 필요
    @JoinColumn(name = "exam_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Exam exam;

    @ManyToOne(fetch = FetchType.EAGER) // TODO: 문제 조회 시 항상 필요해서 일단 EAGER로 설정. 논의 필요
    @JoinColumn(name = "subject_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Subject subject;

    public List<String> getChoices() {
        return Stream.of(choice1, choice2, choice3, choice4, choice5)
                .filter((choice) -> choice != null && !choice.isBlank())
                .toList();
    }
}
