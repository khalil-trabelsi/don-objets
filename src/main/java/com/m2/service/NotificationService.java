package com.m2.service;

import com.m2.model.Notification;
import com.m2.model.Search;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NotificationService {
    Notification createNotification(Notification notification);
    List<Notification> getAllNotificationsByUserId(int userId);

    void deleteSearch(int id);

    public List<Search> getAllSearchByUserId(int userId);
}
