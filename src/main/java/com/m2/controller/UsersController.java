package com.m2.controller;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.UserDto;
import com.m2.model.User;
import com.m2.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getAllUsers(Model model, Authentication authentication) {
        List<UserDto> userDtos = new ArrayList<>();
        if (authentication == null) {
            userDtos = userService.getAllUsers();
        } else {
            userDtos = userService.getAllUsers().stream()
                    .filter(userDto -> !userDto.getUsername().equals(authentication.getName()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("users", userDtos);
        return "users";
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/advertisements")
    public String getAdvertisementsByUserId(@PathVariable int id, Model model) {
        List<AdvertisementDto> advertisementDtos = userService.getAdvertisementsByUserId(id);
        UserDto user = userService.getUserById(id);
        model.addAttribute("advertisements", advertisementDtos);
        model.addAttribute("showFilters", null);
        model.addAttribute("username", user.getUsername());
        return "advertisement";
    }
}
