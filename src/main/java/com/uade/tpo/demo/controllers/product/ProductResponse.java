package com.uade.tpo.demo.controllers.product;
 
import lombok.AllArgsConstructor;
import lombok.Data;

 

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String name;
    private String description;
    private String size;
    private Integer stock;
    private Double price;
    private Double discount;
    private Long categoryId;
    private String message;
}