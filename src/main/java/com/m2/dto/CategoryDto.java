package com.m2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.m2.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    private Integer id;

    @JsonProperty("label")
    @JacksonXmlProperty(localName = "label")
    private String label;

    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
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
        if (categoryDto.getId() != null) {
            category.setId(categoryDto.getId());
        }
        category.setLabel(categoryDto.getLabel());
        category.setDescription(categoryDto.getDescription());

        return category;
    }
}
