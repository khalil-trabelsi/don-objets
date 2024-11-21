package com.m2.service;

import com.m2.dto.CategoryDto;

import java.util.Optional;

public interface CategoryService {
  CategoryDto save(CategoryDto category);

  Optional<CategoryDto> findById(Integer id);

  CategoryDto findByLabel(String label);


}
