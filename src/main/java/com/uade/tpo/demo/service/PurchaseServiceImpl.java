package com.uade.tpo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.controllers.purchase.PurchaseRequest;
import com.uade.tpo.demo.controllers.purchase.PurchaseResponse;
import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public PurchaseResponse purchaseProduct(PurchaseRequest request){
            if(request.getProductId() == null || request.getQuantity() == null){
                throw new IllegalArgumentException("ProductId and Quantity are required");
            }
            if(request.getQuantity() <=0){
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Long userId = getAuthenticatedUserId();

            Product product = productService.getProductById(request.getProductId()).orElseThrow(() -> new ProductNotFoundException());

            Integer newStock = stockService.changeStock(
                request.getProductId(),
                -request.getQuantity()
            );

            Double price = product.getPrice();
            Double discount = product.getDiscount();
            Integer quantity = request.getQuantity();

            Double total = price * quantity * (1 - discount);

            Order order = orderService.createOrder(
                userId,
                product,
                request.getQuantity(),
                total
            );

            return new PurchaseResponse(
                order.getId(),
                product.getId(),
                request.getQuantity(),
                newStock,
                total,
                "Purchase successful"
            );

    }
    
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }

        throw new IllegalStateException("Unable to determine authenticated user");
    }
}