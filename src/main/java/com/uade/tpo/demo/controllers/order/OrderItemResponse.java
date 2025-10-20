package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {


        private ProductResponse product;
        private Integer quantity;
        private Double subtotal;


}
