package com.m2.service.implementation;

import com.m2.dto.CategoryDto;
import com.m2.repository.CategoryRepository;
import com.m2.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class CategoryServiceImplementation implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImplementation(
        CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto save(CategoryDto category) {
        return CategoryDto.fromEntity(
                categoryRepository.save(CategoryDto.toEntity(category))
        );
    }

    public Optional<CategoryDto> findById(Integer id) {
        return categoryRepository.findById(id).map(CategoryDto::fromEntity);
    }

    public CategoryDto findByLabel(String label) {
        return null;
    }
}
