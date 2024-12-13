package com.m2.repository;

import com.m2.model.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    @Query("SELECT a FROM Advertisement a WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR a.keywords LIKE %:keyword%) AND " +
            "(:title IS NULL OR :title = '' OR a.title LIKE %:title%) AND " +
            "(:location IS NULL OR :location = '' OR a.location LIKE %:location%) AND " +
            "(:objectState IS NULL OR :objectState = '' OR a.objectState = :objectState) AND"+
            "(:category IS NULL OR :category = '' OR a.category.label LIKE %:category%)")
    Page<Advertisement> findAdvertisementByFilters(
            String keyword,
            String title,
            String location,
            String objectState,
            String category,
            Pageable pageable);
}
