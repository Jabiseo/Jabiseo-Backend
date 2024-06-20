package com.jabiseo.certificate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject {

    @Id
    @Column(name = "subject_id")
    private String id;

    private String name;

    private int sequence;

    @ManyToOne
    @JoinColumn(name = "certificate_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    private Subject(String id, String name, int sequence, Certificate certificate) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.certificate = certificate;
    }

    public static Subject of(String id, String name, int sequence, Certificate certificate) {
        Subject subject = new Subject(id, name, sequence, certificate);
        certificate.addSubject(subject);
        return subject;
    }

}
