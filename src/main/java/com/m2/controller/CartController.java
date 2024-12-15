package com.m2.controller;

import com.m2.dto.CartDto;
import com.m2.model.Cart;
import com.m2.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public String getCart(@PathVariable int userId, Model model) {
        CartDto cartDto = cartService.getOrCreateCartByUserId(userId);
        model.addAttribute("cart", cartDto);
        return "cart";
    }

    @PostMapping("/{userId}/{advertisementId}")
    public String addAdvertisementToCart(@PathVariable int userId, @PathVariable int advertisementId, HttpServletRequest request) {
        cartService.addAdvertisementToCart(advertisementId, userId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/{cartId}/cartItems/{cartItemId}/remove")
    public String deleteCartItem(@PathVariable int cartId, @PathVariable int cartItemId, HttpServletRequest request) {
        cartService.removeAdvertisementFromCart(cartId, cartItemId);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/{cartId}/remove")
    public String deleteCartItems(@PathVariable int cartId, HttpServletRequest request) {
        cartService.emptyCart(cartId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }



}

