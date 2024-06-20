package com.jabiseo.certificate.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Getter
@Table(name = "certificate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certificate {

    @Id
    @Column(name = "certificate_id")
    private String id;

    private String name;

    @BatchSize(size = 100) // 자격증 당 시험은 100개 이하로 가정
    @OneToMany(mappedBy = "certificate")
    private List<Exam> exams;

    @BatchSize(size = 10) // 자격증 당 과목은 10개 이하로 가정
    @OneToMany(mappedBy = "certificate")
    private List<Subject> subjects;

}
