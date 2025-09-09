package com.uade.tpo.demo.service;

import com.uade.tpo.demo.controllers.purchase.PurchaseRequest;
import com.uade.tpo.demo.controllers.purchase.PurchaseResponse;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.service.ProductService;
import com.uade.tpo.demo.service.PurchaseService;
import com.uade.tpo.demo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Override
    @Transactional
    public PurchaseResponse purchaseProduct(PurchaseRequest request){
            if(request.getProductId() == null || request.getQuantity() == null){
                throw new IllegalArgumentException("ProductId and Quantity are required");
            }
            if(request.getQuantity() <=0){
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Product product = productService.getProductById(request.getProductId()).orElseThrow(() -> new ProductNotFoundException());

            Integer newStock = stockService.changeStock(
                request.getProductId(),
                -request.getQuantity()
            );

            Double price = product.getPrice();
            Double discount = product.getDiscount();
            Integer quantity = request.getQuantity();

            Double total = price * quantity * (1 - discount);

            return new PurchaseResponse(
                product.getId(),
                request.getQuantity(),
                newStock,
                total,
                "Purchase successful"
            );

    }
}