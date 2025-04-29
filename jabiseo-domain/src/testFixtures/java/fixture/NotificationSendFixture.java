package fixture;

import com.jabiseo.domain.notification.domain.Notification;
import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.domain.SendStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class NotificationSendFixture {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String token = "sample-token";

    @Builder.Default
    private String errorMessage = "No error";

    @Builder.Default
    private LocalDateTime sendAt = LocalDateTime.now();

    @Builder.Default
    private SendStatus status = SendStatus.SUCCESS;

    @Builder.Default
    private Notification notification = null;


    public NotificationSendFixture(Long id, String token, String errorMessage, LocalDateTime sendAt, SendStatus status, Notification notification) {
        this.id = id;
        this.token = token;
        this.errorMessage = errorMessage;
        this.sendAt = sendAt;
        this.status = status;
        this.notification = notification;
    }


    public static NotificationSendFixture notificationSend(Notification notification) {
        return NotificationSendFixture.builder()
                .notification(notification)
                .build();
    }

    public static NotificationSendFixture notificationSend() {
        return NotificationSendFixture.builder()
                .build();
    }

    public NotificationSend toEntity() {
        return new NotificationSend(
                this.id,
                this.token,
                this.errorMessage,
                this.sendAt,
                this.status,
                this.notification
        );
    }
}
