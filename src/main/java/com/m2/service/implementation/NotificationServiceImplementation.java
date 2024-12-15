package com.m2.service.implementation;

import com.m2.exception.EntityNotFoundException;
import com.m2.model.Notification;
import com.m2.model.Search;
import com.m2.repository.NotificationRepository;
import com.m2.repository.SearchRepository;
import com.m2.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SearchRepository searchRepository;

    @Autowired
    public NotificationServiceImplementation(NotificationRepository notificationRepository, SearchRepository searchRepository) {
        this.notificationRepository = notificationRepository;
        this.searchRepository =  searchRepository;
    }
    @Override
    public Notification createNotification(Notification notification) {
        return null;
    }

    @Override
    public List<Notification> getAllNotificationsByUserId(int userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    @Override
    public List<Search> getAllSearchByUserId(int userId) {
        return searchRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteSearch(int id) {
        Optional<Search> search = searchRepository.findById(id);

        if (search.isPresent()) {
            List<Notification> notifications = search.get().getNotifications();
            if (notifications != null) {
                notificationRepository.deleteAll(notifications);
            }
            searchRepository.deleteById(id);
        }


    }
}
