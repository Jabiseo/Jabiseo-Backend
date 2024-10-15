package com.jabiseo.problem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProblemInfo {

    @Id
    @Column(name = "problem_info_id")
    private Long id;

    private Long certificateId;

    private String certificateName;

    private Long examId;

    private String examDescription;

    private int examYear;

    private int examYearRound;

    private Long subjectId;

    private String subjectName;

    private int subjectSequence;

}
