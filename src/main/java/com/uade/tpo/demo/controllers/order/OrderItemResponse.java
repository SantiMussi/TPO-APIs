package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.entity.OrderItem;

public record OrderItemResponse(
        Long productId,
        String name,
        Integer quantity,
        Double subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
