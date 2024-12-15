package com.m2.controller;

import com.m2.dto.CategoryDto;
import com.m2.model.Category;
import com.m2.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalModelAttributes {
    private final CategoryService categoryService;

    @Autowired
    public GlobalModelAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("sharedCategories")
    public List<CategoryDto> addCategoryToModel() {
        return categoryService.getAllCategories();
    }

}
