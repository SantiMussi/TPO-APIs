package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;

public interface OrderService {
    Order createOrder(Long userId, Product product, Integer quantity, Double totalPrice);

    List<Order> getOrdersByUser(Long userId);

    Order getOrder(Long orderId);
}