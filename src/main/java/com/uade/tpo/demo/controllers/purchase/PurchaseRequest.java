package com.uade.tpo.demo.controllers.purchase;

import lombok.Data;

import java.util.List;

import com.uade.tpo.demo.entity.PaymentMethod;
import com.uade.tpo.demo.entity.ShippingMethod;

@Data
public class PurchaseRequest{
    private List<Long> productIds;
    private List<Integer> quantities;
    private String couponCode;
    private PaymentMethod paymentMethod;
    private ShippingMethod shippingMethod;
}