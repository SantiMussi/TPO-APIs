package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.entity.Category;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }


    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    //Si algo falla se hace rollback
    @Transactional(rollbackFor = Throwable.class)
    public Product createProduct(String name, String description, String size, int stock, double price, double discount, Long categoryId) {
        Product p = new Product(name, description, size, stock, price, discount);
        if (categoryId != null) {
            Category c = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
            p.setCategory(c);
        }
        return productRepository.save(p);
        //return productRepository.save(new Product(name, description, size, stock, price, discount, category_id));
    }
}
