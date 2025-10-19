package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.exceptions.OrderNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderStatus;
import com.uade.tpo.demo.service.OrderService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return ResponseEntity.ok(OrderDetailResponse.from(order));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> getAllOrders(@RequestParam(required = false) Long userId) {

        List<Order> orders = (userId == null)
                ? orderService.getAllOrders()
                : orderService.getOrdersByUser(userId);

        List<OrderDetailResponse> response = orders.stream()
                .map(OrderDetailResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable Long orderId,
                                                    @RequestParam OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Order " + updatedOrder.getId() + " status changed to: " + status);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
}
}   