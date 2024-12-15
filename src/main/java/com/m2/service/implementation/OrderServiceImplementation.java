package com.m2.service.implementation;

import com.m2.dto.OrderDto;
import com.m2.exception.EntityNotFoundException;
import com.m2.model.*;
import com.m2.repository.*;
import com.m2.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImplementation implements OrderService {
    private final OrderLineRepository orderLineRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final AdvertisementRepository advertisementRepository;


    public OrderServiceImplementation(
            OrderLineRepository orderLineRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            CartRepository cartRepository,
            AdvertisementRepository advertisementRepository
    ) {
        this.orderLineRepository = orderLineRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public List<OrderDto> getOrdersByUserId(int userId) {
        return orderRepository.findAllOrdersByUserId(userId).stream().map(OrderDto::fromEntity).toList();
    }

    @Override
    public OrderDto createOrderFromCart(int userId) {
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user with ID: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create order.");
        }

        Order order = Order.builder()
                .user(cart.getUser())
                .orderDate(new Date())
                .orderLines(new ArrayList<>())
                .build();


        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            OrderLine orderLine = new OrderLine();
            Advertisement advertisement = cartItem.getAdvertisement();
            orderLine.setAdvertisement(advertisement);
            orderLine.setOrder(order);
            orderLineRepository.save(orderLine);
            advertisement.setAvailable(false);
            advertisementRepository.save(advertisement);
        }

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return OrderDto.fromEntity(order);
    }

    @Override
    public OrderDto createOrder(int userId, List<Integer> advertisementIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<Advertisement> advertisements = advertisementRepository.findAllById(advertisementIds);
        if (advertisements.isEmpty()) {
            throw new IllegalArgumentException("No valid advertisements provided.");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(new Date())
                .orderLines(new ArrayList<>())
                .build();
        orderRepository.save(order);

        for (Advertisement advertisement : advertisements) {
            OrderLine orderLine = new OrderLine();
            orderLine.setAdvertisement(advertisement);
            orderLine.setOrder(order);
            orderLineRepository.save(orderLine);
            advertisement.setAvailable(false);
            advertisementRepository.save(advertisement);
        }


        return OrderDto.fromEntity(order);
    }

}
