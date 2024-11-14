package com.m2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m2.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Integer id;

    private String label;

    private String description;



    public static CategoryDto fromEntity(Category category) {
        if (category == null) {
            return null;
            // TODO throw an exception
        }

        return CategoryDto.builder()
                .id(category.getId())
                .label(category.getLabel())
                .description(category.getDescription())
                .build();
    }

    public static Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
            // TODO throw an exception
        }

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setLabel(categoryDto.getLabel());
        category.setDescription(categoryDto.getDescription());

        return category;
    }
}