package com.uade.tpo.demo.controllers.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private Long productId;
    private Integer purchased;
    private Integer stockRemaining;
    private String message;
}