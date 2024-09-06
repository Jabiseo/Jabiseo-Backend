package com.jabiseo.learning.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Learning {

    @Id
    @Tsid
    @Column(name = "learning-id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LearningMode mode;

    private Long learningTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate-id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member-id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;


    private Learning(LearningMode mode, Long learningTime, Certificate certificate, Member member) {
        this.mode = mode;
        this.learningTime = learningTime;
        this.certificate = certificate;
        this.member = member;
    }

    public static Learning of(LearningMode mode, Long learningTime, Certificate certificate, Member member) {
        return new Learning(mode, learningTime, certificate, member);
    }
}
