package com.jabiseo.domain.notification.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification implements Serializable {

    @Id
    @Column(name = "notification_id")
    private Long id;

    private Long memberId;

    @Column(name = "push_type")
    private String pushType;

    private String title;

    private String message;

    private Long certificateId;

    @Builder
    public Notification(Long id, Long memberId, String pushType, String title, String message, Long certificateId) {
        this.id = id;
        this.memberId = memberId;
        this.pushType = pushType;
        this.title = title;
        this.message = message;
        this.certificateId = certificateId;
    }
}
