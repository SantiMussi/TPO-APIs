package com.uade.tpo.demo.controllers.stock;

import lombok.Data;

@Data
public class StockAdjustRequest {
    //Negative, discount. Positive, restock.
    private int quantity;
}
