package com.m2.service;

import com.m2.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
  CategoryDto createCategory(CategoryDto category);

  CategoryDto getCategoryById(Integer id);

  CategoryDto getCategoryByLabel(String label);

  List<CategoryDto> getAllCategories();

  CategoryDto updateCategory(Integer id, CategoryDto categoryDto);

  public void deleteCategoryById(Integer id);

}
