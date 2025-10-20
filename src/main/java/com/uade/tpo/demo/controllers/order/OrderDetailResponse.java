package com.uade.tpo.demo.controllers.order;

import java.util.List;

import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class OrderDetailResponse {

        private Long orderId;
        private Long userId;
        private Double totalPrice;
        private OrderStatus orderStatus;
        private List<OrderItemResponse> items;

}


        /*public record ItemDetail(Long productId, String name, Integer quantity, Double subtotal) {}

        public static OrderDetailResponse from(Order order) {
        List<ItemDetail> details = order.getProducts().stream()
                .map(i -> new ItemDetail(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getSubtotal()))
                .toList();

        return new OrderDetailResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getStatus(),
                details
        );
        }
}*/