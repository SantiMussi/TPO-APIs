package com.uade.tpo.demo.controllers.purchase;

import java.util.List;

import com.uade.tpo.demo.controllers.order.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private Long orderId;
    private Long userId;
    private List<OrderItemResponse> products;
    private Double total;
    private String coupon;
    private String message;
}