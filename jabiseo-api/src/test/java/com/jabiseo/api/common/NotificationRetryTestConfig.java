package com.jabiseo.api.common;

import com.jabiseo.domain.notification.exception.NotificationSendFailedException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@TestConfiguration
public class NotificationRetryTestConfig {

    @Primary
    @Bean
    public Retry notificationRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(10))
                .retryOnException(throwable -> {
                    if (throwable instanceof CompletionException) {
                        throwable = throwable.getCause();
                    }
                    if(throwable instanceof NotificationSendFailedException failedException){
                        return failedException.isRetryable();
                    }
                    return false;
                })
                .build();
        return Retry.of("testNotificationRetry", config);
    }

    @Primary
    @Bean
    public ScheduledExecutorService retryScheduler() {
        return Executors.newScheduledThreadPool(1);
    }
}
