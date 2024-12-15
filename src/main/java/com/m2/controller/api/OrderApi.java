package com.m2.controller.api;

import com.m2.dto.OrderDto;
import com.m2.dto.OrderRequest;
import com.m2.model.User;
import com.m2.service.OrderService;
import com.m2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderApi {

    private final OrderService orderService;

    @Autowired
    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/{userId}")
    public List<OrderDto> getAllOrdersByUserId(@PathVariable int userId) {
        return orderService.getOrdersByUserId(userId);
    }


    @PostMapping("")
    public OrderDto createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest.getUserId(), orderRequest.getAdvertisementsIds());
    }
}
