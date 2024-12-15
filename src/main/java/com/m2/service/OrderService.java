package com.m2.service;

import com.m2.dto.OrderDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderDto createOrderFromCart(int userId);

    List<OrderDto> getOrdersByUserId(int userId);

    @Transactional
    OrderDto createOrder(int userId, List<Integer> advertisementIds);
}
