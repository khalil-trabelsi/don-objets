package com.m2.controller;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.CategoryDto;
import com.m2.model.User;
import com.m2.service.AdvertisementService;
import com.m2.service.CategoryService;
import com.m2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(
            AdvertisementService advertisementService,
            UserService userService,
            CategoryService categoryService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}/advertisements")
    public String getAdvertisementsByCategoryId(@PathVariable int id, Authentication authentication, Model model) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        List<AdvertisementDto> advertisementDtos = new ArrayList<>();
        if (authentication == null) {
            advertisementDtos = advertisementService.getAllByCategoryId(id);
        } else {
            User user = userService.loadUserByUsername(authentication.getName());
            advertisementDtos = advertisementService.getAllByCategoryIdExcludingCurrentUser(id, user.getId());
        }
        model.addAttribute("advertisements", advertisementDtos);

        return "category";
    }
}
