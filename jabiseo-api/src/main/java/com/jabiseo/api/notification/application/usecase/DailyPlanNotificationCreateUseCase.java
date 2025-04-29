package com.jabiseo.api.notification.application.usecase;

import com.jabiseo.api.notification.application.service.DailyPlanNotificationService;
import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DailyPlanNotificationCreateUseCase {

    private final MemberService memberService;
    private final DailyPlanNotificationService dailyPlanNotificationService;
    private static final int PAGE_SIZE = 1000;

    public void executeBatch() {
        Long lastId = 0L;
        boolean hasMore = true;

        while (hasMore) {
            List<MemberDeviceDto> memberList = memberService.findAllWithDeviceTokensByLastIndex(lastId, PAGE_SIZE);
            if (memberList.isEmpty()) {
                break;
            }
            try {
                dailyPlanNotificationService.execute(memberList);
            } catch (Exception e) {
                // 캐치 후 계속 작업 진행 TODO 만약 스프링 배치였다면 작업에 대한 상태를 저장하거나 할 듯
                e.printStackTrace();
                log.error("batch job error occur job info : lastId = {} , pageSize = {} , cause =  {}", lastId, PAGE_SIZE, e.getMessage());
            }
            lastId = memberList.get(memberList.size() - 1).getMember().getId();
        }
    }

}
