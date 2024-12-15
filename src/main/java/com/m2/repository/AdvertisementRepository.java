package com.m2.repository;

import com.m2.dto.AdvertisementDto;
import com.m2.model.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    @Query("SELECT a FROM Advertisement a WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(a.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:title IS NULL OR :title = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR :location = '' OR LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:objectState IS NULL OR :objectState = '' OR LOWER(a.objectState) = LOWER(:objectState)) AND " +
            "(a.available = true) AND " +
            "(:category IS NULL OR :category = '' OR LOWER(a.category.label) LIKE LOWER(CONCAT('%', :category, '%')))")
    Page<Advertisement> findAdvertisementByFilters(
            String keyword,
            String title,
            String location,
            String objectState,
            String category,
            Pageable pageable);



    @Query("SELECT a FROM Advertisement a WHERE a.user.id != :userId and a.available = true")
    Page<Advertisement> findAllExcludingCurrentUser(int userId, Pageable pageable);

    @Query("SELECT a FROM Advertisement a WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(a.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:title IS NULL OR :title = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR :location = '' OR LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:objectState IS NULL OR :objectState = '' OR LOWER(a.objectState) = LOWER(:objectState)) AND " +
            "(:category IS NULL OR :category = '' OR LOWER(a.category.label) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(a.user.id != :userId) AND a.available = true"
    )
    Page<Advertisement> findAdvertisementExcludingCurrentUserByFilters(
            int userId,
            String keyword,
            String title,
            String location,
            String objectState,
            String category,
            Pageable pageable);

    @Query("SELECT a FROM Advertisement a WHERE a.user.id = :userId and a.available = true")
    List<Advertisement> findAllByUserId(int userId);

    @Query("SELECT a FROM Advertisement a WHERE a.user.id = :userId")
    List<Advertisement> findAllByCurrentUserId(int userId);

    @Query("SELECT a FROM Advertisement a WHERE a.category.id = :categoryId AND a.user.id != :userId AND a.available")
    List<Advertisement> findAllByCategoryIdExcludingCurrentUser(int categoryId, int userId);

    @Query("SELECT a FROM Advertisement a WHERE a.category.id = :categoryId")
    List<Advertisement> findAllByCategoryId(int categoryId);

}
