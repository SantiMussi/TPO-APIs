package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.List;

public interface ProductService {
    public Page<Product> getProducts(PageRequest pageRequest);

    public Optional<Product> getProductById(Long productId);

    public Product createProduct(String name, String description, String size, int stock, double price, double discount, Long category_id);
}
