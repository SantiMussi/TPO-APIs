package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.PurchaseItem;
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

import java.util.List;

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
    public PurchaseResponse purchaseProduct(PurchaseRequest request) {

        List<PurchaseItem> items = request.getItems();
        Long userId = getAuthenticatedUserId();

        List<Integer> quantities = items.stream().map(PurchaseItem::getQuantity).toList();

        int newStock = 0;
        Order order = null;

        List<Product> products = null;

        double total = 0.0;

        for (PurchaseItem item : items){
            if (item.getProductId() == null || item.getQuantity() == null) throw new IllegalArgumentException("ProductId and Quantity are required");

            if (item.getQuantity() <=0) throw new IllegalArgumentException("Quantity must be greater than zero");

            Product product = productService.getProductById(item.getProductId()).orElseThrow(ProductNotFoundException::new);

            products.add(product);

            newStock = stockService.changeStock(
                    item.getProductId(),
                    -item.getQuantity()
            );

            Double price = product.getPrice();
            double discount = product.getDiscount();
            Integer quantity = item.getQuantity();

            total = total + price * quantity * (1 - discount);
        }

        order = orderService.createOrder(
                userId,
                products,
                quantities,
                total
        );

        List<Long> productIds = items.stream().map(PurchaseItem::getProductId).toList();

        return new PurchaseResponse(
                order.getId(),
                productIds,
                quantities,
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