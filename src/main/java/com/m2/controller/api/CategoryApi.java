package com.m2.controller.api;

import com.m2.dto.Categories;
import com.m2.dto.CategoryDto;
import com.m2.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@Slf4j
public class CategoryApi {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<Categories> getAllCategories() {
        List<CategoryDto> categories = this.categoryService.findAll();
        return ResponseEntity.ok().body(new Categories(categories));
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<CategoryDto> getCategoryByLabel(@PathVariable Integer id) {
        Optional<CategoryDto> category = categoryService.findById(id);
        return category.map(categoryDto -> ResponseEntity.ok().body(categoryDto)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    @PostMapping
    public CategoryDto save(@RequestBody  CategoryDto categoryDto, HttpServletRequest request) {
        log.info("Creating category: " + categoryDto);
        log.error("request headers: " + request.getHeader("Content-Type"));
        return categoryService.save(categoryDto);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable  Integer id) {
        categoryService.deleteById(id);
    }

}
