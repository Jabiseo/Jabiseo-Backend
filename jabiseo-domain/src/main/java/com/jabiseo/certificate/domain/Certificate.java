package com.jabiseo.certificate.domain;


import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certificate {

    @Id
    @Column(name = "certificate_id")
    private String id;

    private String name;

    @BatchSize(size = 100) // 자격증 당 시험은 100개 이하로 가정
    @OneToMany(mappedBy = "certificate")
    private List<Exam> exams = new ArrayList<>();

    @BatchSize(size = 10) // 자격증 당 과목은 10개 이하로 가정
    @OneToMany(mappedBy = "certificate")
    private List<Subject> subjects = new ArrayList<>();

    private Certificate(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Certificate of(String id, String name) {
        return new Certificate(id, name);
    }

    public void addExam(Exam exam) {
        exams.add(exam);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public boolean containsSubject(String subjectId) {
        return subjects.stream()
                .map(Subject::getId)
                .anyMatch(id -> id.equals(subjectId));
    }

    public boolean containsExam(String examId) {
        return exams.stream()
                .map(Exam::getId)
                .anyMatch(id -> id.equals(examId));
    }

    public void validateSubjectIds(List<String> subjectIds) {
        // 자격증에 해당하는 과목들이 모두 있는지 검사
        subjectIds.forEach(subjectId -> {
            if (!this.containsSubject(subjectId)) {
                throw new CertificateBusinessException(CertificateErrorCode.SUBJECT_NOT_FOUND_IN_CERTIFICATE);
            }
        });
    }

    public void validateExamId(Optional<String> examId) {
        // 자격증에 해당하는 시험이 있는지 검사
        if (examId.isPresent() && !this.containsExam(examId.get())) {
            throw new CertificateBusinessException(CertificateErrorCode.EXAM_NOT_FOUND_IN_CERTIFICATE);
        }
    }

    public void validateAndSubjectIds(Optional<String> examId, List<String> subjectIds) {
        validateExamId(examId);
        validateSubjectIds(subjectIds);
    }
}
