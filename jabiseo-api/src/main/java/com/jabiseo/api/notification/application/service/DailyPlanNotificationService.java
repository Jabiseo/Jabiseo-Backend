package com.jabiseo.api.notification.application.service;

import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.notification.domain.*;
import com.jabiseo.domain.notification.dto.NotificationInfo;
import com.jabiseo.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanNotificationService {

    private final NotificationAsyncService notificationAsyncService;
    private final NotificationService notificationService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(List<MemberDeviceDto> members) {
        log.info("start job - > member size = {}", members.size());

        List<NotificationInfo> notificationInfoJoins = applyNotificationCheckAsync(members);
        batchInsert(notificationInfoJoins);

        log.info(" test -> " + notificationInfoJoins.size());
        List<CompletableFuture<Void>> sendFutures = sendNotificationAsync(notificationInfoJoins);

        // Blocking (모든 알림 전송 완료될 때까지 대기)
        CompletableFuture.allOf(sendFutures.toArray(new CompletableFuture[0])).join();

    }

    private List<CompletableFuture<Void>> sendNotificationAsync(List<NotificationInfo> notificationInfos){
        return notificationInfos.stream()
                .map(info ->
                        notificationAsyncService.sendAsync(SendNotificationCommand.from(info.getNotification(), info.getNotificationSend()))
                                .thenAccept(result -> {
                                    // 이 부분은 DailyPlanNotificationService 책임
                                    notificationService.updateSend(result.getSendId(), result.getStatus());
                                })
                ).toList();
    }

    private List<NotificationInfo> applyNotificationCheckAsync(List<MemberDeviceDto> members) {
        List<CompletableFuture<List<NotificationInfo>>> futures = members.stream().map(member ->
                notificationService.notifyPlan(member)).toList();
        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList())
                .join();
    }


    private void batchInsert(List<NotificationInfo> infos) {
        List<Notification> notificationList = infos.stream().map(NotificationInfo::getNotification).toList();
        List<NotificationSend> notificationSendList = infos.stream().map(NotificationInfo::getNotificationSend).toList();
        notificationService.batchSaveAll(notificationList);
        notificationService.batchSendSaveAll(notificationSendList);
    }
}
