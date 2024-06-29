package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "problem")
    private List<Bookmark> bookmarks = new ArrayList<>();

    public List<String> getChoices() {
        return Stream.of(choice1, choice2, choice3, choice4, choice5)
                .filter((choice) -> choice != null && !choice.isBlank())
                .toList();
    }

    public Problem(String id, String description, String choice1, String choice2, String choice3,
                   String choice4, String choice5, int answerNumber, String theory, String solution,
                   Certificate certificate, Exam exam, Subject subject) {
        this.id = id;
        this.description = description;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
        this.answerNumber = answerNumber;
        this.theory = theory;
        this.solution = solution;
        this.certificate = certificate;
        this.exam = exam;
        this.subject = subject;
    }

    public static Problem of(String id, String description, String choice1, String choice2, String choice3,
                             String choice4, String choice5, int answerNumber, String theory, String solution,
                             Certificate certificate, Exam exam, Subject subject) {
        return new Problem(id, description, choice1, choice2, choice3, choice4, choice5,
                answerNumber, theory, solution, certificate, exam, subject);
    }

    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
    }
}
