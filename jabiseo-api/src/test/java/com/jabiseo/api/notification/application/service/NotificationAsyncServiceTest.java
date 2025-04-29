package com.jabiseo.api.notification.application.service;

import com.jabiseo.api.common.NotificationRetryTestConfig;
import com.jabiseo.api.config.async.AsyncConfig;
import com.jabiseo.domain.common.IdempotencyChecker;
import com.jabiseo.domain.notification.domain.NotificationSender;
import com.jabiseo.domain.notification.domain.SendNotificationCommand;
import com.jabiseo.domain.notification.domain.SendResult;
import com.jabiseo.domain.notification.domain.SendStatus;
import com.jabiseo.domain.notification.exception.NotificationSendFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@EnableAsync
@Tag("TooLongTest")
@SpringBootTest(
        classes = {NotificationAsyncService.class, NotificationRetryTestConfig.class, AsyncConfig.class}
)
@ActiveProfiles("test")
@Import(NotificationRetryTestConfig.class)
class NotificationAsyncServiceTest {

    @Autowired
    private NotificationAsyncService notificationAsyncService;

    @MockBean
    private NotificationSender notificationSender;

    @MockBean
    private IdempotencyChecker idempotencyChecker;

    private int attemptCount;

    @BeforeEach
    void setUp() {
        attemptCount = 0;
        // 항상 멱등성 통과
        Mockito.when(idempotencyChecker.check(anyString(), anyInt())).thenReturn(true);
    }

    @Test
    @DisplayName("Retry가 2번 실패 후 성공하면 SUCCESS 상태를 반환한다")
    void retrySucceedsAfterFailures() throws Exception {
        // given
        SendNotificationCommand command = new SendNotificationCommand(1L, 1L, "title", "body", "type", 1L, 1L, "tokens");

        Mockito.when(notificationSender.sendAsync(eq(command)))
                .thenAnswer(invocation -> {
                    attemptCount++;
                    if (attemptCount < 3) {
                        return CompletableFuture.failedFuture(new NotificationSendFailedException("전송 실패", true, null));
                    } else {
                        return CompletableFuture.completedFuture("mock-message-id");
                    }
                });


        // when
        CompletableFuture<SendResult> future = notificationAsyncService.sendAsync(command);
        SendResult result = future.get(5, TimeUnit.SECONDS);

        // then
        assertThat(result.getStatus()).isEqualTo(SendStatus.SUCCESS);
        assertThat(result.getMessageId()).isEqualTo("mock-message-id");
        assertThat(attemptCount).isEqualTo(3); // Retry가 정확히 3번 수행됨
    }

    @Test
    @DisplayName("Retry 최대 횟수 이후에도 실패하면 FAILED 상태를 반환한다")
    void retryFailsCompletely() throws Exception {
        // given
        SendNotificationCommand command = new SendNotificationCommand(1L, 1L, "title", "body", "type", 1L, 1L, "tokens");

        Mockito.when(notificationSender.sendAsync(eq(command)))
                .thenAnswer(invocation -> {
                    attemptCount++;
                    return CompletableFuture.failedFuture(new NotificationSendFailedException("전송 실패", true, null));
                });

        // when
        CompletableFuture<SendResult> future = notificationAsyncService.sendAsync(command);
        SendResult result = future.get(5, TimeUnit.SECONDS);

        // then
        assertThat(result.getStatus()).isEqualTo(SendStatus.FAILED);
        assertThat(result.getMessageId()).isNull();
        assertThat(attemptCount).isEqualTo(3); // 최대 시도 횟수만큼만 시도함
    }

}
