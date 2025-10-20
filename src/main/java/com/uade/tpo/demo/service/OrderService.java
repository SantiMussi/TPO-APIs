package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderStatus;
import com.uade.tpo.demo.exceptions.OrderNotFoundException;

public interface OrderService {

    List<Order> getOrdersByUser(Long userId);

    Order getOrder(Long orderId) throws OrderNotFoundException;

    Order createOrder(Order order);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long orderId, OrderStatus status) throws OrderNotFoundException;
}