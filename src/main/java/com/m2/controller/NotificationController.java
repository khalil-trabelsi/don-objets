package com.m2.controller;


import com.m2.model.Notification;
import com.m2.model.Search;
import com.m2.model.User;
import com.m2.service.NotificationService;
import com.m2.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService  notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService  notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("")
    public String getNotificationByUserId(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = userService.loadUserByUsername(authentication.getName());
        List<Search> searches = notificationService.getAllSearchByUserId(user.getId());
        model.addAttribute("searches", searches);
        return "notification";
    }

    @GetMapping("/delete/{id}")
    public String deleteSearch(@PathVariable int id) {
        notificationService.deleteSearch(id);
        return "redirect:/notification";
    }
}
