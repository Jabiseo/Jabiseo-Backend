package com.jabiseo.api.config.retry;

import com.jabiseo.domain.notification.exception.NotificationSendFailedException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class NotificationRetryConfig {
    private static final String NOTIFICATION_RETRY_CONFIG_NAME = "notificationRetry";

    @Bean
    public RetryConfig retryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
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
    }

    @Bean
    public RetryRegistry retryRegistry(RetryConfig retryConfig) {
        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        retryRegistry.addConfiguration(NOTIFICATION_RETRY_CONFIG_NAME, retryConfig);
        return retryRegistry;
    }

    @Bean
    public Retry notificationRetry(RetryRegistry retryRegistry) {
        return retryRegistry.retry("fcmNotificationSender", NOTIFICATION_RETRY_CONFIG_NAME);
    }

    @Bean
    ScheduledExecutorService retryScheduler() {
        return Executors.newScheduledThreadPool(4);
    }
}
