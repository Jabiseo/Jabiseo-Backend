package com.jabiseo.domain.notification.domain;

import lombok.Getter;

@Getter
public class SendResult {
    private Long sendId;
    private SendStatus status;
    private String messageId;
    private String error;

    public SendResult(Long sendId, SendStatus status, String messageId, String error) {
        this.sendId = sendId;
        this.status = status;
        this.messageId = messageId;
        this.error = error;
    }
}
