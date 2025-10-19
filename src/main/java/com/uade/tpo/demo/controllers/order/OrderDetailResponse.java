package com.uade.tpo.demo.controllers.order;

import java.util.List;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderStatus;

public record OrderDetailResponse(
        Long orderId,
        Long userId,
        Double totalPrice,
        OrderStatus orderStatus,
        List<ItemDetail> items) {

        public record ItemDetail(Long productId, String name, Integer quantity, Double subtotal) {}

        public static OrderDetailResponse from(Order order) {
        List<ItemDetail> details = order.getProducts().stream()
                .map(i -> new ItemDetail(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getSubtotal()))
                .toList();

        return new OrderDetailResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getStatus(),
                details
        );
        }
}