package com.jabiseo.domain.notification.dto;

import com.jabiseo.domain.notification.domain.Notification;
import com.jabiseo.domain.notification.domain.NotificationSend;
import lombok.Getter;

@Getter
public class NotificationInfo {

    private Notification notification;
    private NotificationSend notificationSend;

    public NotificationInfo(Notification notification, NotificationSend notificationSend) {
        this.notification = notification;
        this.notificationSend = notificationSend;
    }
}
