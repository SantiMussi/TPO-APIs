package com.uade.tpo.demo.controllers.product;
 
import com.uade.tpo.demo.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

 

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long creatorId;
    private Size size;
    private Integer stock;
    private Double price;
    private Double discount;
    private String base64img;

}