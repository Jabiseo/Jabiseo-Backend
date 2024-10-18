package com.jabiseo.notification;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(topics = "${notification.topic}", groupId = "${notification.group-id}")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
