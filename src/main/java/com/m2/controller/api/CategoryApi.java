package com.m2.controller.api;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.Categories;
import com.m2.dto.CategoryDto;
import com.m2.service.AdvertisementService;
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
@RequestMapping("/api/categories")
@AllArgsConstructor
@Slf4j
public class CategoryApi {

    private final CategoryService categoryService;
    private final AdvertisementService advertisementService;


    @GetMapping()
    public ResponseEntity<Categories> getAllCategories() {
        List<CategoryDto> categories = this.categoryService.getAllCategories();
        return ResponseEntity.ok().body(new Categories(categories));
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok().body(categoryDto);

    }

    @PostMapping
    public CategoryDto createCatgeory(@RequestBody  CategoryDto categoryDto, HttpServletRequest request) {
        log.info("Creating category: " + categoryDto);
        log.error("request headers: " + request.getHeader("Content-Type"));
        return categoryService.createCategory(categoryDto);
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
        categoryService.deleteCategoryById(id);
    }

    @GetMapping(path= "/{id}/advertisements")
    public List<AdvertisementDto> getAdvertisementsByCategoryId(@PathVariable int id) {
        return advertisementService.getAllByCategoryId(id);
    }



}
