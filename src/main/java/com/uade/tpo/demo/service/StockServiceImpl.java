package com.uade.tpo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private ProductRepository productRepository;

    public int getStock(long productId) {
       Product p = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
       
                if(p.getStock() == 0) throw new IllegalStateException("No hay stock del producto.");
       return p.getStock();
    }


    @Transactional
    public int changeStock(long productId, int quantity){
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        
        int newStock = p.getStock() + quantity;
        if (newStock < 0) throw new IllegalStateException("Stock insuficiente.");
        p.setStock(newStock);
        productRepository.save(p);
        return p.getStock();
    }
}
