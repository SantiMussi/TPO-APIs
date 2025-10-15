package com.uade.tpo.demo.controllers.product;

import com.uade.tpo.demo.entity.Size;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Size size;
    private Integer stock;
    private Double price;
    private Double discount;
    private Long categoryId;
    private String base64img;
}
