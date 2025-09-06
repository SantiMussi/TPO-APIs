package com.uade.tpo.demo.controllers.product;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String size;
    private int stock;
    private double price;
    private double discount;
    private Long categoryId;
}
