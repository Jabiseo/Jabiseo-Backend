package com.jabiseo.domain.notification.service;

import com.jabiseo.domain.member.domain.Member;
import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.notification.domain.*;
import com.jabiseo.domain.notification.dto.NotificationInfo;
import com.jabiseo.domain.notification.repository.NotificationBatchRepository;
import com.jabiseo.domain.notification.repository.NotificationRepository;
import com.jabiseo.domain.notification.repository.NotificationSendRepository;
import com.jabiseo.domain.plan.domain.Plan;
import com.jabiseo.domain.plan.dto.PlanCompletedResult;
import com.jabiseo.domain.plan.service.PlanProgressService;
import com.jabiseo.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSendRepository notificationSendRepository;
    private final PlanProgressService planProgressService;
    private final PlanService planService;
    private final NotificationBatchRepository notificationBatchRepository;

    @Transactional
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    public void saveAll(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void sendSaveAll(List<NotificationSend> sends) {
        notificationSendRepository.saveAll(sends);
    }

    @Transactional
    public void batchSaveAll(List<Notification> notifications) {
        notificationBatchRepository.saveAll(notifications);
    }

    @Transactional
    public void batchSendSaveAll(List<NotificationSend> notificationSends) {
        notificationBatchRepository.sendSaveAll(notificationSends);
    }

    @Transactional
    public void batchSendUpdate(List<SendResult> sendResults) {
        notificationBatchRepository.updateAllSendResult(sendResults);
    }


    public void updateSend(Long sendId, SendStatus sendStatus) {
        notificationSendRepository.updateStatusById(sendId, sendStatus);
    }

    public Optional<NotificationSend> findSendById(Long id) {
        return notificationSendRepository.findById(id);
    }


    @Async("notificationTaskExecutor")
    public CompletableFuture<List<NotificationInfo>> notifyPlan(MemberDeviceDto memberDeviceDto) {
        Optional<Plan> plan = planService.findPlanByMember(memberDeviceDto.getMember());
        if (plan.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        LocalDate date = LocalDate.now().minusDays(1);

        PlanCompletedResult result = planProgressService.checkPlanProgressIsCompleted(plan.get(), date);

        if(result.isProgressEmpty()){
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        Notification notification = NotificationFactory.createPlanDailyPush(plan.get(), result.isCompleted());


        List<NotificationInfo> infos = memberDeviceDto.getDeviceTokenList().stream()
                .map(it -> NotificationFactory.createSend(notification, it, LocalDateTime.now()))
                .map(send -> new NotificationInfo(notification, send))
                .toList();

        return CompletableFuture.completedFuture(infos);
    }
}
