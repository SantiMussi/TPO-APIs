package com.uade.tpo.demo.controllers.categories;

import com.uade.tpo.demo.controllers.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String description;
    private List<ProductResponse> product;
}
