package com.jabiseo.notification.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSender {


    public void sendMessage(com.jabiseo.domain.notification.domain.Notification notification) throws FirebaseMessagingException, JsonProcessingException {
        Notification fcmNotification = Notification
                .builder()
                .setTitle(notification.getPushType().getTitle())
                .setBody(notification.getPushType().getMessage())
                .build();

        Message message = Message.builder()
                .setToken(notification.getToken())
                .putData("certificateId", notification.getRedirectId().toString())
                .setNotification(fcmNotification)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info(response);
    }
}
