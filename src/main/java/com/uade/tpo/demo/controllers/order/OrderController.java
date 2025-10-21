package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.controllers.product.ImageManager;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.entity.OrderItem;
import com.uade.tpo.demo.exceptions.OrderNotFoundException;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderStatus;
import com.uade.tpo.demo.service.OrderService;


@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);

            LinkedList<OrderItemResponse> products = new LinkedList<>();

            for (OrderItem product : order.getProducts()) {

                ProductResponse productResponse = new ProductResponse(
                        product.getProduct().getId(),
                        product.getProduct().getName(),
                        product.getProduct().getDescription(),
                        product.getProduct().getCategory().getId(),
                        product.getProduct().getCategory().getDescription(),
                        product.getProduct().getCreatorId(),
                        product.getProduct().getSize(),
                        product.getProduct().getStock(),
                        product.getProduct().getPrice(),
                        product.getProduct().getDiscount(),
                        ImageManager.fileToBase64(product.getProduct().getImg())
                );

                products.add(new OrderItemResponse(
                        productResponse,
                        product.getQuantity(),
                        product.getSubtotal()
                ));
            }

            OrderDetailResponse response = new OrderDetailResponse(
                    order.getId(),
                    order.getUser().getId(),
                    order.getIssueDate(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    products);


            return ResponseEntity.ok(response);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<OrderDetailResponse>> getAllOrders(@RequestParam(required = false) Long userId) {

        try {
            List<Order> orders = orderService.getAllOrders();

            LinkedList<OrderDetailResponse> orderDetailResponses = new LinkedList<>();

            for (Order order : orders) {
                orderDetailResponses.add(getOrder(order.getId()).getBody());
            }

            return ResponseEntity.ok(new PageImpl<>(orderDetailResponses));
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<OrderDetailResponse>> getUserOrders(@PathVariable Long userId){
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);

            LinkedList<OrderDetailResponse> orderDetailResponses = new LinkedList<>();

            for (Order order : orders) {
                orderDetailResponses.add(getOrder(order.getId()).getBody());
            }

            return ResponseEntity.ok(new PageImpl<>(orderDetailResponses));
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }

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