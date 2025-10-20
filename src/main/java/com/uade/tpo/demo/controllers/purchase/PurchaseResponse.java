package com.uade.tpo.demo.controllers.purchase;

import java.util.List;

import com.uade.tpo.demo.controllers.order.OrderItemResponse;

public record PurchaseResponse(
        Long orderId,
        Long userId,
        List<OrderItemResponse> products,
        Double total,
        String coupon,
        String message
) {}