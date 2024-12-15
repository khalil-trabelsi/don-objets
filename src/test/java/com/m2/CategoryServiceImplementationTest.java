package com.m2;

import com.m2.dto.CategoryDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Category;
import com.m2.repository.CategoryRepository;
import com.m2.service.implementation.CategoryServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplementationTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImplementation categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1);
        category.setLabel("Electronics");
        category.setDescription("Category for electronic items");

        categoryDto = new CategoryDto();
        categoryDto.setId(1);
        categoryDto.setLabel("Electronics");
        categoryDto.setDescription("Category for electronic items");
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.createCategory(categoryDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getLabel(), result.getLabel());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Act
        CategoryDto result = categoryService.getCategoryById(1);

        // Assert
        assertNotNull(result);
        assertEquals(category.getLabel(), result.getLabel());
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        // Arrange
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryById(1);
        });

        assertEquals("Category with 1Not found", exception.getMessage());
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act
        List<CategoryDto> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(category.getLabel(), result.get(0).getLabel());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        categoryDto.setDescription("Updated Description");
        CategoryDto updatedCategory = categoryService.updateCategory(1, categoryDto);

        // Assert
        assertNotNull(updatedCategory);
        assertEquals("Updated Description", updatedCategory.getDescription());
        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        CategoryDto updatedCategory = categoryService.updateCategory(1, categoryDto);

        // Assert
        assertNull(updatedCategory);
        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    void testDeleteCategoryById() {
        // Arrange
        doNothing().when(categoryRepository).deleteById(1);

        // Act
        categoryService.deleteCategoryById(1);

        // Assert
        verify(categoryRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCategoryByIdNullId() {
        // Act
        categoryService.deleteCategoryById(null);

        // Assert
        verify(categoryRepository, times(0)).deleteById(any());
    }
}
