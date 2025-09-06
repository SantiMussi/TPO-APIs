package com.uade.tpo.demo.controllers.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponse {
    private Long productId;
    private int stock;
}