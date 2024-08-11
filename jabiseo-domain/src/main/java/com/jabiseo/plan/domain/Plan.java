package com.jabiseo.plan.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<PlanItem> planItems = new ArrayList<>();

    public Plan(Certificate certificate, Member member, LocalDate endAt) {
        this.certificate = certificate;
        this.member = member;
        this.endAt = endAt;
    }

    public static Plan create(Member member, LocalDate endAt){
        return new Plan(member.getCurrentCertificate(), member, endAt);
    }
}
