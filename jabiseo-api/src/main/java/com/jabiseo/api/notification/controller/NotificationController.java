package com.jabiseo.api.notification.controller;


import com.jabiseo.api.notification.application.usecase.DailyPlanNotificationCreateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/*
 * Test API
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "local"})
public class NotificationController {

    private final DailyPlanNotificationCreateUseCase dailyPlanNotificationCreateUseCase;


    @GetMapping("/api/notifications/batch")
    public ResponseEntity<String> processBatch() {
        long startTime = System.currentTimeMillis();
        log.info("start test => ");
        dailyPlanNotificationCreateUseCase.executeBatch();
        log.info("end test ");
        long endTime = System.currentTimeMillis();
        log.info("Testing notification Time is  " + (endTime - startTime) / 1000.0 + "s");
        return ResponseEntity.ok()
                .body("Success + " + (endTime - startTime) / 1000.0 + "s");
    }

}
