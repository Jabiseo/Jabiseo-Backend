package com.jabiseo.api.notification.fake;

import com.jabiseo.api.helper.fake.AlwaysSuccessStrategy;
import com.jabiseo.api.helper.fake.FailStrategy;
import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.notification.domain.Notification;
import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.dto.NotificationInfo;
import com.jabiseo.domain.notification.repository.NotificationBatchRepository;
import com.jabiseo.domain.notification.repository.NotificationRepository;
import com.jabiseo.domain.notification.repository.NotificationSendRepository;
import com.jabiseo.domain.notification.service.NotificationService;
import com.jabiseo.domain.plan.service.PlanProgressService;
import com.jabiseo.domain.plan.service.PlanService;
import lombok.Builder;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


public class FakeFailNotificationService extends NotificationService {

    private AtomicInteger counter = new AtomicInteger(0);
    private FailStrategy failStrategy = new AlwaysSuccessStrategy();


    public AtomicInteger sendSaveAllCount = new AtomicInteger(0);
    public AtomicInteger saveAllCount = new AtomicInteger(0);

    public void setFailStrategy(FailStrategy failStrategy) {
        this.counter.set(0);
        this.failStrategy = failStrategy;
    }

    public void clear(){
        this.counter.set(0);
        this.sendSaveAllCount.set(0);
        this.saveAllCount.set(0);
        this.failStrategy = new AlwaysSuccessStrategy();
    }

    @Builder
    public FakeFailNotificationService(NotificationRepository notificationRepository,
                                       NotificationSendRepository notificationSendRepository,
                                       PlanProgressService planProgressService,
                                       PlanService planService,
                                       NotificationBatchRepository notificationBatchRepository) {
        super(notificationRepository, notificationSendRepository, planProgressService, planService, notificationBatchRepository);
    }

    @Override
    @Async("notificationTaskExecutor")
    public CompletableFuture<List<NotificationInfo>> notifyPlan(MemberDeviceDto memberDeviceDto) {
        int count = counter.incrementAndGet();

        if (failStrategy.shouldFail(count, memberDeviceDto.getMember().getId())) {
            throw new RuntimeException("DB Error");
        }
        return super.notifyPlan(memberDeviceDto);
    }

    @Override
    public void sendSaveAll(List<NotificationSend> sends) {
        sendSaveAllCount.incrementAndGet();
        super.sendSaveAll(sends);
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        saveAllCount.incrementAndGet();
        super.saveAll(notifications);
    }
}
