package com.jabiseo.member.domain;

import com.jabiseo.certificate.domain.Certificate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE member_id = ?")
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    private String email;

    private String nickname;

    private String profileImage;

    private String oauthId;

    private String oauthServer;

    private boolean deleted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    // TODO : @LastAccessedDate 만들어서 적용해보기
    private LocalDateTime lastAccessAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_state_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Certificate certificateState;

}
