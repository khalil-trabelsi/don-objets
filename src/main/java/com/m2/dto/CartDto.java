package com.m2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.m2.model.Cart;
import com.m2.model.CartItem;
import com.m2.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {
    private int id;
    private User user;
    private List<CartItem> cartItems;


    public static CartDto fromEntity(Cart cart) {
        if (cart == null) {
            return null;
        }
        return CartDto.builder()
                .id(cart.getId())
                .cartItems(cart.getCartItems())
                .user(cart.getUser())
                .build();
    }

    public static Cart toEntity(CartDto cartDto) {
        if (cartDto == null) {
            return null;
        }
        return Cart.builder()
                .id(cartDto.getId())
                .cartItems(cartDto.getCartItems())
                .user(cartDto.getUser())
                .build();
    }

}
