package com.jabiseo.domain.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_send")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSend {

    @Id
    @Column(name = "notification_send_id")
    private Long id;

    private String token;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime sendAt;

    @Enumerated(EnumType.STRING)
    private SendStatus status;

    @ManyToOne
    @JoinColumn(name = "notification_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Notification notification;

    @Builder
    public NotificationSend(Long id, String token, String errorMessage, LocalDateTime sendAt, SendStatus status, Notification notification) {
        this.id = id;
        this.token = token;
        this.errorMessage = errorMessage;
        this.sendAt = sendAt;
        this.status = status;
        this.notification = notification;
    }
}
