package com.jabiseo.infra.fcm;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.jabiseo.domain.notification.domain.*;
import com.jabiseo.domain.notification.exception.NotificationSendFailedException;
import com.jabiseo.infra.utils.ApiFutureConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"local", "test"})
public class FcmNotificationSenderImpl implements NotificationSender {


    @Override
    public CompletableFuture<String> sendAsync(SendNotificationCommand command) {
        log.info("[FCM] Sending notification to FCM {}, member => {} , thread => {}", command.getId(), command.getMemberId(), Thread.currentThread().getName());
        Message message = Message.builder()
                .setToken(command.getToken())
                .putData("certificateId", command.getCertificatedId().toString())
                .putData("type", command.getPushType())
                .setNotification(buildNotification(command))
                .build();

        ApiFuture<String> apiFuture = FirebaseMessaging.getInstance().sendAsync(message, true);
        return ApiFutureConverter.toCompletableFuture(apiFuture)
                .exceptionally(throwable -> {
                    if(throwable instanceof CompletionException) {
                        throwable = throwable.getCause();
                    }
                    if(throwable instanceof FirebaseMessagingException fme){
                        boolean retryable =  isRetryableErrorCode(fme.getMessagingErrorCode());
                        throw new NotificationSendFailedException(
                                "FCM Send Fail: " + fme.getMessage(),
                                retryable,
                                fme
                        );
                    }
                    throw new NotificationSendFailedException(
                            "UNKNOWN",
                            false,
                            throwable
                    );
                });
    }

    private boolean isRetryableErrorCode(MessagingErrorCode errorCode){
        return errorCode.equals(MessagingErrorCode.INTERNAL) || errorCode.equals(MessagingErrorCode.UNAVAILABLE);
    }


    private com.google.firebase.messaging.Notification buildNotification(SendNotificationCommand command) {
        return com.google.firebase.messaging.Notification.builder()
                .setTitle(command.getTitle())
                .setBody(command.getMessage())
                .build();
    }
}
