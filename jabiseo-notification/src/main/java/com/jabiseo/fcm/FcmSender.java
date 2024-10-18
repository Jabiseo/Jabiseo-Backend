package com.jabiseo.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSender {


    public void sendMessage(String token) throws FirebaseMessagingException, JsonProcessingException {
        String title = "titles...";
        String body = "bodys...";
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("id", "12345");
        data.put("type", "PLAN");
        data.put("certificateId", "2");

        Message message = Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info(response);
    }
}
