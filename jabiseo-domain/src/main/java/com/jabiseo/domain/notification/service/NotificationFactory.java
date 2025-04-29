package com.jabiseo.domain.notification.service;

import com.jabiseo.domain.member.domain.DeviceToken;
import com.jabiseo.domain.notification.domain.Notification;
import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.domain.PushType;
import com.jabiseo.domain.notification.domain.SendStatus;
import com.jabiseo.domain.plan.domain.Plan;
import io.hypersistence.tsid.TSID;

import java.time.LocalDateTime;

public class NotificationFactory {

    public static Notification createPlanDailyPush(Plan plan, boolean isCompleted) {
        return Notification.builder()
                .id(TSID.fast().toLong())
                .memberId(plan.getMember().getId())
                .pushType(isCompleted ? PushType.DAILY_PLAN_COMPLETED.getType() : PushType.DAILY_PLAN_INCOMPLETE.getType())
                .title(isCompleted ? PushType.DAILY_PLAN_COMPLETED.getTitle() :  PushType.DAILY_PLAN_INCOMPLETE.getTitle())
                .message(isCompleted ? PushType.DAILY_PLAN_COMPLETED.getMessage() : PushType.DAILY_PLAN_INCOMPLETE.getMessage())
                .certificateId(plan.getCertificate().getId())
                .build();
    }


    public static NotificationSend createSend(Notification notification, DeviceToken deviceToken, LocalDateTime sendAt) {
        return NotificationSend.builder()
                .id(TSID.fast().toLong())
                .notification(notification)
                .token(deviceToken.getToken())
                .status(SendStatus.UNKNOWN)
                .sendAt(sendAt)
                .build();
    }
}
