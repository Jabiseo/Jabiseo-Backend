package com.jabiseo.domain.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendNotificationCommand {

    private Long id;
    private Long memberId;
    private String pushType;
    private String title;
    private String message;
    private Long certificatedId;
    private Long sendId;
    private String token;



    @Builder
    public SendNotificationCommand(Long id, Long memberId,String pushType, String title, String message, Long certificatedId, Long sendId, String token) {
        this.id = id;
        this.memberId = memberId;
        this.pushType = pushType;
        this.title = title;
        this.message = message;
        this.certificatedId = certificatedId;
        this.sendId = sendId;
        this.token = token;
    }


    public static SendNotificationCommand from(Notification notification, NotificationSend notificationSend) {
        return SendNotificationCommand.builder()
                .id(notification.getId())
                .memberId(notification.getMemberId())
                .pushType(notification.getPushType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .certificatedId(notification.getCertificateId())
                .sendId(notificationSend.getId())
                .token(notificationSend.getToken())
                .build();
    }

}
