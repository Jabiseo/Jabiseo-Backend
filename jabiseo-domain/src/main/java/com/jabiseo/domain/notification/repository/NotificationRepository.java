package com.jabiseo.domain.notification.repository;

import com.jabiseo.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
