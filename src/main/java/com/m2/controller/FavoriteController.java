package com.m2.controller;

import com.m2.model.Advertisement;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.UserRepository;
import com.m2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/favorites")
@AllArgsConstructor
@Slf4j
public class FavoriteController {

    public final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public String getFavorites(
            Authentication authentication,
            Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userService.loadUserByUsername(authentication.getName());

        List<Advertisement> advertisements = new ArrayList<>(user.getFavorites());
        model.addAttribute("advertisements", advertisements);
        model.addAttribute("userId", user.getId());
        model.addAttribute("favorites", user.getFavorites().stream().map(Advertisement::getId).collect(Collectors.toList()));
        model.addAttribute("showFilters", false);
        return "advertisement";
    }


    @GetMapping("/add")
    public String addToFavorites(
            @RequestParam(defaultValue = "0") int advertisementId,
            Authentication authentication) {
        log.info("authentification {}", authentication);
        if (authentication == null ) {
            return "redirect:/login";
        }

        User user = userService.loadUserByUsername(authentication.getName());

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));

        if (user.getFavorites() == null) {
            user.setFavorites(new ArrayList<>());
            System.out.println("User added to Favorites"+user.getFavorites());
        }
        log.warn("{}", user.getFavorites());
        if (user.getFavorites().contains(advertisement)) {
            user.getFavorites().remove(advertisement);
        } else {
            user.getFavorites().add(advertisement);
        }

        userRepository.save(user);

        return "redirect:/";
    }
}
