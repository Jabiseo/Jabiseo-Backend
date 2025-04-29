package fixture;

import com.jabiseo.domain.notification.domain.Notification;
import lombok.Builder;

@Builder
public class NotificationFixture {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private Long memberId = 100L;

    @Builder.Default
    private String pushType = "DEFAULT";

    @Builder.Default
    private String title = "기본 제목";

    @Builder.Default
    private String message = "기본 메시지";

    @Builder.Default
    private Long certificateId = 1L;

    public NotificationFixture(Long id, Long memberId, String pushType, String title, String message, Long certificateId) {
        this.id = id;
        this.memberId = memberId;
        this.pushType = pushType;
        this.title = title;
        this.message = message;
        this.certificateId = certificateId;
    }

    public static NotificationFixture notification(){
        return NotificationFixture.builder().build();
    }

    public Notification toEntity(){
        return new Notification(
                this.id,
                this.memberId,
                this.pushType,
                this.title,
                this.message,
                this.certificateId
        );
    }

}
