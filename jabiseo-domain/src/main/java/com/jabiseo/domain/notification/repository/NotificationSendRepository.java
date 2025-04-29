package com.jabiseo.domain.notification.repository;

import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.domain.SendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationSendRepository extends JpaRepository<NotificationSend, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE NotificationSend s SET s.status = :status WHERE s.id = :id
    """)
    void updateStatusById(@Param("id") Long id, @Param("status") SendStatus status);
}
