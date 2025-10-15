package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Size;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.List;

public interface ProductService {
    public Page<Product> getProducts(PageRequest pageRequest);

    public Optional<Product> getProductById(Long productId);

    public Product createProduct(String name, String description, Size size, int stock, double price, double discount, Long category_id, String img) throws ProductDuplicateException;

    public Product changeProductInfo(Long prodId, String name, String description, Size size, Integer stock, Double price, Double discount, Long categoryId) throws ProductNotFoundException;

    public void deleteProduct(Long productId) throws ProductNotFoundException;
}
