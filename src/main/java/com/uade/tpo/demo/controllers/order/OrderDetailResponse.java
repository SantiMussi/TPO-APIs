package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.entity.Order;

public record OrderDetailResponse(
        Long orderId,
        Long userId,
        Long productId,
        String productName,
        Integer quantity,
        Double totalPrice) {

    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getUser().getId(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalPrice());
    }
}