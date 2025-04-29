package com.jabiseo.api.notification.application.service;

import com.jabiseo.api.helper.fake.FailAtSpecificCountStrategy;
import com.jabiseo.api.notification.fake.FakeFailNotificationService;
import com.jabiseo.api.common.NotificationIntegrationTestConfig;
import com.jabiseo.domain.member.domain.Member;
import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.notification.domain.*;
import com.jabiseo.domain.notification.repository.NotificationRepository;
import com.jabiseo.domain.notification.repository.NotificationSendRepository;
import fixture.DeviceTokenFixture;
import fixture.MemberFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@Slf4j
@EnableAsync
@Tag("TooLongTest") //
@ActiveProfiles("test")
@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/daily-plan-notification-create-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Import(NotificationIntegrationTestConfig.class)
class DailyPlanNotificationServiceTest {

    @Autowired
    private DailyPlanNotificationService dailyPlanNotificationService;


    @MockBean
    private NotificationAsyncService notificationAsyncService;

    @Autowired
    private FakeFailNotificationService fakeFailNotificationService;

    @Autowired
    private NotificationSendRepository notificationSendRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    @BeforeEach
    public void setup() {
    }

    @AfterEach
    void tearDown() {
        notificationSendRepository.deleteAll();
        notificationRepository.deleteAll();
        fakeFailNotificationService.clear();
    }

    @Test
    @DisplayName("알림 전송 작업 성공시 SUCCESS로 저장된다")
    void sendNotificationSuccess() throws Exception {
        //given
        List<MemberDeviceDto> memberDevices = createMemberDeviceDtoByIds(
                List.of(1L, 2L)
        );
        given(notificationAsyncService.sendAsync(any())).willReturn(CompletableFuture.completedFuture(new SendResult(1L, SendStatus.SUCCESS, "", null)));
        given(notificationAsyncService.sendAsync(any())).willReturn(CompletableFuture.completedFuture(new SendResult(2L, SendStatus.SUCCESS, "", null)));

        //when
        dailyPlanNotificationService.execute(memberDevices);

        //then
        List<NotificationSend> result = notificationSendRepository.findAll();

        assertThat(result)
                .allSatisfy(send -> assertThat(send.getStatus()).isEqualTo(SendStatus.SUCCESS));

    }

    @Test
    @DisplayName("알림 전송 실패시 FAILED로 저장된다")
    void sendNotificationFail() {
        //given
        List<MemberDeviceDto> memberDevices = createMemberDeviceDtoByIds(
                List.of(1L, 2L)
        );
        given(notificationAsyncService.sendAsync(any())).willReturn(CompletableFuture.completedFuture(new SendResult(1L, SendStatus.FAILED, "", null)));
        given(notificationAsyncService.sendAsync(any())).willReturn(CompletableFuture.completedFuture(new SendResult(2L, SendStatus.FAILED, "", null)));


        //when
        dailyPlanNotificationService.execute(memberDevices);

        //then
        List<NotificationSend> result = notificationSendRepository.findAll();
        result.forEach(notificationSend -> {
            assertThat(notificationSend.getStatus()).isEqualTo(SendStatus.FAILED);
        });
    }


    @Test
    @DisplayName("notificationService에서 예외 발생시 롤백한다")
    void whenNotificationServiceThrownException_Success_RollBack() throws Exception {
        //given
        List<MemberDeviceDto> memberDevices = createMemberDeviceDtoByIds(
                List.of(1L, 2L)
        );
        // 2번째 작업 수행시 예외를 반환함
        fakeFailNotificationService.setFailStrategy(new FailAtSpecificCountStrategy(2));

        //when
        assertThatThrownBy(() -> dailyPlanNotificationService.execute(memberDevices))
                .as("예외 발생시 예와가 전파된다.")
                .isInstanceOf(RuntimeException.class);

        //then
        List<NotificationSend> result = notificationSendRepository.findAll();
        assertThat(result).hasSize(0);
    }


    private List<MemberDeviceDto> createMemberDeviceDtoByIds(List<Long> ids) {
        List<MemberDeviceDto> memberDevices = new ArrayList<>();

        for (Long id : ids) {
            Member member = MemberFixture.createMember(id);
            memberDevices.add(new MemberDeviceDto(member, List.of(
                    DeviceTokenFixture.createDeviceToken(member)
            )));
        }
        return memberDevices;
    }
}
