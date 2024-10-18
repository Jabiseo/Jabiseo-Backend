package com.jabiseo.notification;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@ConfigurationProperties(prefix = "notification")
public class NotificationConfigProperty {

    private final String groupId;
    private final String topic;

    public NotificationConfigProperty(String groupId, String topic) {
        this.groupId = groupId;
        this.topic = topic;
    }

}
