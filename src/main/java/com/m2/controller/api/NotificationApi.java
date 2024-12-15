package com.m2.controller.api;

import com.m2.model.Notification;
import com.m2.model.Search;
import com.m2.service.NotificationService;
import com.m2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApi {

    private final NotificationService notificationService;

    @Autowired
    public NotificationApi(NotificationService  notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public List<Search> get(@PathVariable int userId){
        return this.notificationService.getAllSearchByUserId(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteSearch(@PathVariable int userId){
        this.notificationService.deleteSearch(userId);
    }


}
