package com.jabiseo.api.notification.application.service;


import com.jabiseo.domain.common.IdempotencyChecker;
import com.jabiseo.domain.notification.domain.NotificationSender;
import com.jabiseo.domain.notification.domain.SendNotificationCommand;
import com.jabiseo.domain.notification.domain.SendResult;
import com.jabiseo.domain.notification.domain.SendStatus;
import com.jabiseo.domain.notification.exception.AlreadySendNotificationException;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAsyncService {

    private final Retry notificationRetry;
    private final ScheduledExecutorService retryScheduler;
    private final NotificationSender notificationSender;
    private final IdempotencyChecker idempotencyChecker;
    private static final String KET_PREFIX = "notification:send:";
    private static final int EXPIRES_IN_HOUR = 1;

    @Async("notificationFCMExecutor")
    public CompletableFuture<SendResult> sendAsync(SendNotificationCommand command) {
        log.info("[FCM] Sending notification to FCM {}, member => {} , thread => {}", command.getId(), command.getMemberId(), Thread.currentThread().getName());
        String key = KET_PREFIX + command.getSendId().toString();

        return notificationRetry.executeCompletionStage(retryScheduler, () -> {
                    if (!idempotencyChecker.check(key, EXPIRES_IN_HOUR)) {
                        throw new AlreadySendNotificationException();
                    }
                    return notificationSender.sendAsync(command)
                            .whenComplete((result, tx) -> {
                                if (tx != null) {
                                    idempotencyChecker.reset(key);
                                }
                            });
                })
                .toCompletableFuture()
                .thenApply(messageId -> new SendResult(command.getSendId(), SendStatus.SUCCESS, messageId, null))
                .exceptionally(ex -> new SendResult(command.getSendId(), SendStatus.FAILED, null, ex.getMessage()));
    }
}
