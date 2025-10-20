package com.uade.tpo.demo.service;

import com.uade.tpo.demo.exceptions.InvalidStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private ProductRepository productRepository;

    public int getStock(long productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        if(p.getStock() == 0) throw new InvalidStockException();
        
        return p.getStock();
    }


    @Transactional
    public int changeStock(long productId, int quantity) throws InvalidStockException {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        
        int newStock = p.getStock() + quantity;
        if (newStock < 0) throw new InvalidStockException();

        p.setStock(newStock);
        productRepository.save(p);
        return p.getStock();
    }
}
