package com.m2.dto;

import com.m2.model.Order;
import com.m2.model.OrderLine;
import com.m2.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private int id;
    private Date orderDate;
    private User user;
    private List<OrderLine> orderLines;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .orderLines(order.getOrderLines())
                .user(order.getUser())
                .build();
    }

    public static Order toEntity(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .orderDate(orderDto.getOrderDate())
                .orderLines(orderDto.getOrderLines())
                .user(orderDto.getUser())
                .build();
    }
}
