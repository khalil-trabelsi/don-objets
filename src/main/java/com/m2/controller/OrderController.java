package com.m2.controller;

import com.m2.dto.OrderDto;
import com.m2.model.User;
import com.m2.service.OrderService;
import com.m2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String orders(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        User user = userService.loadUserByUsername(authentication.getName());
        return "redirect:/orders/" + user.getId();
    }
    @GetMapping("/{userId}")
    public String getAllOrdersByUserId(@PathVariable int userId, Model model) {
        List<OrderDto> orderDtos = orderService.getOrdersByUserId(userId);
        model.addAttribute("orders", orderDtos);
        return "orders";
    }


    @PostMapping
    public String createOrderFromCart(Authentication authentication) {
            if (authentication == null) {
                return "redirect:/login";
            }
            User user = userService.loadUserByUsername(authentication.getName());
            orderService.createOrderFromCart(user.getId());

        return "redirect:/orders";
    }

}
