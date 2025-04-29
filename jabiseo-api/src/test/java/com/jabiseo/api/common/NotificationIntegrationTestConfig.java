package com.jabiseo.api.common;

import com.jabiseo.api.notification.fake.FakeFailNotificationService;
import com.jabiseo.domain.notification.repository.NotificationBatchRepository;
import com.jabiseo.domain.notification.repository.NotificationRepository;
import com.jabiseo.domain.notification.repository.NotificationSendRepository;
import com.jabiseo.domain.plan.service.PlanProgressService;
import com.jabiseo.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@RequiredArgsConstructor
public class NotificationIntegrationTestConfig {

    private final NotificationRepository notificationRepository;
    private final NotificationSendRepository notificationSendRepository;
    private final NotificationBatchRepository notificationBatchRepository;
    private final PlanProgressService planProgressService;
    private final PlanService planService;

    @Primary
    @Bean(name = "fakeNotificationService")
    public FakeFailNotificationService fakeNotificationService() {
        return FakeFailNotificationService
                .builder()
                .notificationRepository(notificationRepository)
                .notificationSendRepository(notificationSendRepository)
                .notificationBatchRepository(notificationBatchRepository)
                .planProgressService(planProgressService)
                .planService(planService)
                .build();
    }


}
