package com.m2.controller;

import com.m2.dto.CategoryDto;
import com.m2.dto.UserDto;
import com.m2.model.Category;
import com.m2.model.User;
import com.m2.service.CategoryService;
import com.m2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * This controller is responsible for sharing data between all HTML templates
 */
@ControllerAdvice
@Slf4j
public class GlobalModelAttributes {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public GlobalModelAttributes(
            CategoryService categoryService,
            UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @ModelAttribute("sharedCategories")
    public List<CategoryDto> addCategoryToModel() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("authenticatedUser")
    public User addUserToModel(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return userService.loadUserByUsername(authentication.getName());
    }

}
