package com.jabiseo.domain.notification.domain;

import java.util.concurrent.CompletableFuture;

public interface NotificationSender {


    CompletableFuture<String> sendAsync(SendNotificationCommand command);

}
