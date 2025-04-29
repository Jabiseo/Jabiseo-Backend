package com.jabiseo.domain.notification.repository;

import com.jabiseo.domain.notification.domain.Notification;
import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.domain.SendResult;
import com.jabiseo.domain.notification.domain.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    // SQL 정의
    private static final String INSERT_NOTIFICATION_SQL = "INSERT INTO notification (notification_id, member_id, push_type, title, message, certificate_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NOTIFICATION_SEND_SQL = "INSERT INTO notification_send (notification_send_id, token, error_message, send_at, status, notification_id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_NOTIFICATION_SEND_SQL = "UPDATE notification_send SET status = ?, error_message = ? WHERE notification_send_id = ?";
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAllSendResult(List<SendResult> sendResults){

        jdbcTemplate.batchUpdate(
                UPDATE_NOTIFICATION_SEND_SQL,
                sendResults,
                sendResults.size(),
                (PreparedStatement ps, SendResult result) -> {
                    ps.setString(1, result.getStatus().name());
                    ps.setString(2, result.getError());     // null 가능
                    ps.setLong(3, result.getSendId());
                }
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(List<Notification> notifications) {
        int[] rows = jdbcTemplate.batchUpdate(INSERT_NOTIFICATION_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Notification notification = notifications.get(i);
                ps.setLong(1, notification.getId());
                ps.setLong(2, notification.getMemberId());
                ps.setString(3, notification.getPushType());
                ps.setString(4, notification.getTitle());
                ps.setString(5, notification.getMessage());
                ps.setLong(6, notification.getCertificateId());
            }

            @Override
            public int getBatchSize() {
                return notifications.size();
            }
        });

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendSaveAll(List<NotificationSend> notificationSends) {
        int[] rows = jdbcTemplate.batchUpdate(INSERT_NOTIFICATION_SEND_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                NotificationSend notificationSend = notificationSends.get(i);
                ps.setLong(1, notificationSend.getId());
                ps.setString(2, notificationSend.getToken());
                ps.setString(3, notificationSend.getErrorMessage());
                ps.setObject(4, notificationSend.getSendAt());
                ps.setString(5, notificationSend.getStatus().name());
                ps.setLong(6, notificationSend.getNotification().getId());
            }

            @Override
            public int getBatchSize() {
                return notificationSends.size();
            }
        });

    }
}
