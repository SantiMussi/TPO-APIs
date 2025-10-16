package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.OrderNotFoundException;

public interface OrderService {
    Order createOrder(Long userId, List<Long> productIds, List<Integer> quantities);

    List<Order> getOrdersByUser(Long userId);

    Order getOrder(Long orderId) throws OrderNotFoundException;
}