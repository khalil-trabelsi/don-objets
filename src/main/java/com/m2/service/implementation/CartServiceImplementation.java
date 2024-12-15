package com.m2.service.implementation;

import com.m2.dto.CartDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.Advertisement;
import com.m2.model.Cart;
import com.m2.model.CartItem;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.CartItemRepository;
import com.m2.repository.CartRepository;
import com.m2.repository.UserRepository;
import com.m2.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImplementation implements CartService {
    private final CartRepository cartRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;


    @Autowired
    public CartServiceImplementation(
            CartRepository cartRepository,
            AdvertisementRepository advertisementRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository
    ) {
        this.cartRepository = cartRepository;
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public CartDto createCart(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with Id"+userId+" not found"));
        Cart cart = cartRepository.save(Cart.builder().user(user).build());
        return CartDto.fromEntity(cart);
    }


    public CartDto getOrCreateCartByUserId(int userId) {
        return cartRepository.findCartByUserId(userId).map(CartDto::fromEntity)
                .orElseGet(()-> this.createCart(userId));
    }

    public CartDto getCartById(int id) {
        return cartRepository.findById(id).map(CartDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException("Cart not found"));
    }

    public void addAdvertisementToCart(int advertisementId, int userId) {
        Advertisement advertisement = advertisementRepository
                .findById(advertisementId).orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));


        Optional<Cart> existingCart = cartRepository.findCartByUserId(userId);
        Cart cart;
        if (existingCart.isEmpty()) {
            cart = new Cart();
            cartRepository.save(cart);
        } else {
            cart = existingCart.get();
        }

        List<Integer> associatedAdvertisementsIds = cart.getCartItems().stream().map(cartItem -> cartItem.getAdvertisement().getId()).toList();
        log.info("{}", associatedAdvertisementsIds.contains(advertisement.getId()));
        if (!associatedAdvertisementsIds.contains(advertisement.getId())) {
            CartItem cartItem = CartItem.builder().advertisement(advertisement).cart(cart).build();
            cartItemRepository.save(cartItem);
        }

    }


    @Override
    public void removeAdvertisementFromCart(int cartId, int cartItemId) {
        Cart cart = cartRepository.
                findById(cartId).orElseThrow(()-> new EntityNotFoundException("Cart not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        cartRepository.save(cart);
    }

    @Override
    public void emptyCart(int cartId) {
        Cart cart = cartRepository.
                findById(cartId).orElseThrow(()-> new EntityNotFoundException("Cart not found"));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();

        cartRepository.save(cart);

    }

}
