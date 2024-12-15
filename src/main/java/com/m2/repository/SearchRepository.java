package com.m2.repository;

import com.m2.model.Notification;
import com.m2.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Integer> {
    List<Search> findAllByUserId(int userId);

}
