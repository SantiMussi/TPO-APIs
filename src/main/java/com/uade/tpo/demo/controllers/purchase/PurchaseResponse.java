package com.uade.tpo.demo.controllers.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private Long orderId;
    private List<Long> productId;
    private List<Integer> purchased;
    private Integer stockRemaining;
    private Double total;
    private String message;
}