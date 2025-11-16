package com.uade.tpo.demo.controllers.purchase;

import java.time.LocalDate;
import java.util.List;

import com.uade.tpo.demo.controllers.order.OrderItemResponse;
import com.uade.tpo.demo.entity.PaymentMethod;
import com.uade.tpo.demo.entity.ShippingMethod;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private Long orderId;
    private Long userId;
    private LocalDate issueDate;
    private List<OrderItemResponse> products;
    private Double total;
    private String coupon;
    private PaymentMethod paymentMethod;
    private ShippingMethod shippingMethod;
    private String message;
}