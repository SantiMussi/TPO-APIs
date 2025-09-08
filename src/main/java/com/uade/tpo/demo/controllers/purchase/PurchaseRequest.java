package com.uade.tpo.demo.controllers.purchase;

import lombok.Data;

@Data
public class PurchaseRequest{
    private Long productId;
    private Integer quantity;
}