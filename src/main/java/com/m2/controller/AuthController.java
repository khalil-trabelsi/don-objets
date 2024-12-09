package com.m2.controller;

import com.m2.dto.UserDto;
import com.m2.repository.UserRepository;
import com.m2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.m2.model.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthController {

    private UserService userService;
    @GetMapping(value="login")
    public String login() {
        return "login";
    }

    @GetMapping(value="register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(path = "save")
    public String save(Model model, @ModelAttribute User user) {
        log.info(user.getPassword());
        userService.save(UserDto.fromEntity(user));
        model.addAttribute("user", new User());
        model.addAttribute("success", true);
        return "register";
    }
}
