package com.jabiseo.certificate.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "certificate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certificate {

    @Id
    @Column(name = "certificate_id")
    private String id;

    private String name;

    @Builder
    public Certificate(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
