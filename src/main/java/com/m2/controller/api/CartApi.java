package com.m2.controller.api;


import com.m2.dto.CartDto;
import com.m2.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartApi {
    private final CartService cartService;

    @Autowired
    public CartApi(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public CartDto getCart(@PathVariable int userId) {
        return cartService.getOrCreateCartByUserId(userId);
    }

    @PutMapping("/{userId}/{advertisementId}")
    public void addAdvertisementToCart(@PathVariable int userId, @PathVariable int advertisementId) {
        cartService.addAdvertisementToCart(advertisementId, userId);

    }

    @DeleteMapping("/{cartId}/cartItems/{cartItemId}/remove")
    public void deleteCartItem(@PathVariable int cartId, @PathVariable int cartItemId) {
        cartService.removeAdvertisementFromCart(cartId, cartItemId);
    }

    @DeleteMapping("/{cartId}/remove")
    public void deleteCartItems(@PathVariable int cartId) {
        cartService.emptyCart(cartId);
    }
}
