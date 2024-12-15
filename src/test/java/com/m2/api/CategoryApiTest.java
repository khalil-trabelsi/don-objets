package com.m2.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m2.controller.api.CategoryApi;
import com.m2.dto.CategoryDto;
import com.m2.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryApi categoryApi;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(
                new CategoryDto(1, "Updated Category", "updated description"),
                new CategoryDto(2, "voitures", "op")
        ));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].label").value("Updated Category"))
                .andExpect(jsonPath("$.categories[1].label").value("voitures"));
    }


    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1)).thenReturn(new CategoryDto(1, "Category 1", "test"));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Updated Category"));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDto newCategory = new CategoryDto(null, "New Category", "new description");
        when(categoryService.createCategory(newCategory)).thenReturn(new CategoryDto(3, "New Category", "new description"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("New Category"));
    }


    @Test
    void testUpdateCategory() throws Exception {
        CategoryDto updatedCategory = new CategoryDto(1, "Updated Category", "updated description");
        when(categoryService.updateCategory(1, updatedCategory)).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Updated Category"))
                .andExpect(jsonPath("$.description").value("updated description"));
    }

}
