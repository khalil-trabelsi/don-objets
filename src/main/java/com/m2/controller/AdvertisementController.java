package com.m2.controller;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.CategoryDto;
import com.m2.dto.UserDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.*;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.CategoryRepository;
import com.m2.repository.NotificationRepository;
import com.m2.repository.SearchRepository;
import com.m2.service.AdvertisementService;
import com.m2.service.CategoryService;
import com.m2.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final AdvertisementService advertisementService;
    private final SearchRepository searchRepository;
    private final NotificationRepository notificationRepository;

    @GetMapping
    public String get(Model model,
                      Authentication authentication,
                      @RequestParam(name = "page", defaultValue = "0") int page,
                      @RequestParam(name = "size", defaultValue = "8") int size,
                      @RequestParam(name = "title", required = false) String title,
                      @RequestParam(name = "keyword", required = false) String keyword,
                      @RequestParam(name = "location", required = false) String location,
                      @RequestParam(name = "objectState", required = false) String objectState,
                      @RequestParam(name = "category", required = false) String category
    ) {
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userService.loadUserByUsername(authentication.getName());
            model.addAttribute("userId", user.getId());
            model.addAttribute("favorites", user.getFavorites().stream().map(Advertisement::getId).collect(Collectors.toList()));
        }
        Page<AdvertisementDto> advertisementsPage = advertisementService.getAdvertisementByFilters(user,keyword, title, location, objectState,category, page, size);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("advertisements", advertisementsPage);
        model.addAttribute("pages", new int[advertisementsPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("objectState", objectState);
        model.addAttribute("title", title);
        model.addAttribute("category", category);
        model.addAttribute("showFilters", true);
        return "advertisement";
    }

    @GetMapping("deposer-une-annonce")
    public String getAdvertisementForm(Model model, Authentication authentication, @PathParam(value="false") boolean addCategorySuccess) {
        if (authentication == null) {
            return "redirect:/login";
        }

        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("advertisement", new Advertisement());
        model.addAttribute("newCategory", new CategoryDto());
        model.addAttribute("addCategorySuccess", addCategorySuccess);
        return "advertisementCreationForm";
    }

    @PostMapping("/createAdvertisement")
    public String createAdvertisement(Model model, @ModelAttribute AdvertisementDto advertisementDto, Authentication authentication) {
        User user = userService.loadUserByUsername(authentication.getName());
        advertisementDto.setUser(UserDto.fromEntity(user));
        advertisementService.createAdvertisement(advertisementDto);
        model.addAttribute("advertisement", new AdvertisementDto());
        return "redirect:/";
    }


    @GetMapping("/mes-annonces")

    public String getAllAdvertisementsByUserId(Authentication authentication, Model model) {
        if (authentication ==  null) {
            return "redirect:/login";
        }
        User user = userService.loadUserByUsername(authentication.getName());
        List<AdvertisementDto> advertisementDtos = advertisementService.getAllAdvertisementsByUserId(user.getId());
        model.addAttribute("advertisements", advertisementDtos);
        return "my-advertisement";
    }

    @GetMapping("/advertisement/delete")
    public String deleteAdvertisement(@RequestParam int id, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User currentUser = userService.loadUserByUsername(authentication.getName());
        AdvertisementDto advertisementDto = advertisementService.getAdvertisementById(id);
        if(advertisementDto.getUser().getId() != currentUser.getId()) {
            return "errorPage";
        }
        advertisementService.deleteAdvertisement(id);

        return "redirect:/mes-annonces";
    }

    @GetMapping("/advertisement/edit")
    public String getUpdateAdvertisementForm(@RequestParam int id, Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User currentUser = userService.loadUserByUsername(authentication.getName());
        AdvertisementDto advertisementDto = advertisementService.getAdvertisementById(id);
        if(advertisementDto.getUser().getId() != currentUser.getId()) {
            return "errorPage";
        }

        List<CategoryDto> categoryDtos = categoryService.getAllCategories();

        model.addAttribute("advertisementDto", advertisementDto);
        model.addAttribute("categoryId", advertisementDto.getCategory().getId());
        model.addAttribute("categories", categoryDtos);
        return "updateAdvertisement";
    }

    @PostMapping("/advertisement/edit/{id}")
public String updateAdvertisement(@PathVariable  int id,@ModelAttribute AdvertisementDto advertisementDto, Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        AdvertisementDto updatedAdvertisementDto = advertisementService.updateAdvertisement(id, advertisementDto);
        model.addAttribute("advertisement", updatedAdvertisementDto);
        return "redirect:/mes-annonces";
    }

}
