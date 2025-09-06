package com.uade.tpo.demo.service;

public interface StockService {
    int getStock(long productId);
    int changeStock(long productId, int quantity); //Negativo descuenta, positivo repone el stock
}
