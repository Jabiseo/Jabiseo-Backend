package com.jabiseo.certificate.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "certificate")
    private List<Exam> exams;

    @OneToMany(mappedBy = "certificate")
    private List<Subject> subjects;

}
