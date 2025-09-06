package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ProductRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }


    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    //Si algo falla se hace rollback
    @Transactional(rollbackFor = Throwable.class)
    public Product createProduct(String name, String description, String size, int stock, double price, double discount) {
        return productRepository.save(new Product(name, description, size, stock, price, discount));
    }
}
