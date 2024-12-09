package com.m2.service.implementation;

import com.m2.dto.CategoryDto;
import com.m2.model.Category;
import com.m2.repository.CategoryRepository;
import com.m2.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImplementation(
        CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto save(CategoryDto category) {
        return CategoryDto.fromEntity(
                categoryRepository.save(CategoryDto.toEntity(category))
        );
    }

    @Override
    public Optional<CategoryDto> findById(Integer id) {
        return categoryRepository.findById(id).map(CategoryDto::fromEntity);
    }
    @Override
    public CategoryDto findByLabel(String label) {
        return null;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::fromEntity).collect(Collectors.toList());
    };

    @Override
    public   CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            Category foundCategory = category.get();
            foundCategory.setDescription(categoryDto.getDescription());
            foundCategory.setLabel(categoryDto.getLabel());
            return CategoryDto.fromEntity(categoryRepository.save(foundCategory));
        }

       return null;
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            log.error("id is null");
            return;
        }
        categoryRepository.deleteById(id);
    }

}
