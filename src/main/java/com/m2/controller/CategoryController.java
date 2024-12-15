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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<AdvertisementDto> advertisementDtos;
        if (authentication == null) {
            advertisementDtos = advertisementService.getAllByCategoryId(id);
        } else {
            User user = userService.loadUserByUsername(authentication.getName());
            advertisementDtos = advertisementService.getAllByCategoryIdExcludingCurrentUser(id, user.getId());
        }
        model.addAttribute("category", categoryDto);
        model.addAttribute("advertisements", advertisementDtos);

        return "category";
    }

    @PostMapping
    public String createCategory(@ModelAttribute CategoryDto categorydto, RedirectAttributes redirectAttributes) {
        categoryService.createCategory(categorydto);
        redirectAttributes.addAttribute("addCategorySuccess", true);
        return "redirect:/deposer-une-annonce";
    }
}
