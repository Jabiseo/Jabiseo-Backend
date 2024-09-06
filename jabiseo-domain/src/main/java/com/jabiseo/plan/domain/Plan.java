package com.jabiseo.plan.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {

    @Id
    @Tsid
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    private LocalDate endAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @BatchSize(size = 100)
    private List<PlanItem> planItems = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;


    public Plan(Certificate certificate, Member member, LocalDate endAt) {
        this.certificate = certificate;
        this.member = member;
        this.endAt = endAt;
    }

    public static Plan create(Member member, LocalDate endAt){
        return new Plan(member.getCurrentCertificate(), member, endAt);
    }
}
