package com.m2.service;

import com.m2.dto.CartDto;

public interface CartService {
    CartDto getOrCreateCartByUserId(int userId);

    CartDto createCart(int userId);
    CartDto getCartById(int id);
    void addAdvertisementToCart(int advertisementId, int userId);
    void removeAdvertisementFromCart(int cartId, int cartItemId);
    void emptyCart(int cartId);

}
