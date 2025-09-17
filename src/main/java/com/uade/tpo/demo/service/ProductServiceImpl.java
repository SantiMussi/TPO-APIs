package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Size;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductResponse;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;
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
    public Product createProduct(String name, String description, Size size, int stock, double price, double discount, Long categoryId) throws ProductDuplicateException {
        Product p = new Product(name, description, size, stock, price, discount);
        if (categoryId != null) {
            Category c = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
            p.setCategory(c);
        }
        if (!productRepository.existsDuplicate(name, description, size, price)) {
            return productRepository.save(p);
        }

        throw new ProductDuplicateException();
    }

    @Transactional
    @Override
    public Product changeProductInfo (Long prodId, String name, String description, Size size, Integer stock, Double price, Double discount, Long categoryId) throws ProductNotFoundException{
        Product p = productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException());

        if (name != null) p.setName(name);
        if (description != null) p.setDescription(description);
        if (size != null) p.setSize(size);
        if (stock != null) p.setStock(stock);
        if (price != null) p.setPrice(price);
        if (discount != null) p.setDiscount(discount);
        if (categoryId != null){
            Category c = categoryRepository.getById(categoryId); 
            p.setCategory(c);
        } 

        
        return productRepository.save(p);
    }
    
    @Override
    public void deleteProduct(Long productId){
        Product p = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException());
        productRepository.delete(p); 
    }
    
    
}
