package com.m2.repository;

import com.m2.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c where c.user.id = :userId")
    Optional<Cart> findCartByUserId(int userId);
}
