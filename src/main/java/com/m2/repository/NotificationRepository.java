package com.m2.repository;

import com.m2.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findNotificationBySearchId(int search);

    List<Notification> findAllByUserId(int userId);
}
