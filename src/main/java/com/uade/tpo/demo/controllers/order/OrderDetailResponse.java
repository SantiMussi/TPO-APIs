package com.uade.tpo.demo.controllers.order;

import java.time.LocalDate;
import java.util.List;

import com.uade.tpo.demo.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class OrderDetailResponse {

        private Long orderId;
        private Long userId;
        private LocalDate issueDate;
        private Double totalPrice;
        private OrderStatus orderStatus;
        private List<OrderItemResponse> items;

}