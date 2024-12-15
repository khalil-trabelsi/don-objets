package com.m2.repository;

import com.m2.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o where o.user.id = :userId")
    List<Order> findAllOrdersByUserId(int userId);
}
