package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.PurchaseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.controllers.order.OrderItemResponse;
import com.uade.tpo.demo.controllers.purchase.PurchaseRequest;
import com.uade.tpo.demo.controllers.purchase.PurchaseResponse;
import com.uade.tpo.demo.entity.Coupon;
import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderItem;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.repository.CouponRepository;
import com.uade.tpo.demo.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Override
    @Transactional
    public PurchaseResponse purchaseProduct(PurchaseRequest request) {

        if (request.getProductIds() == null || request.getQuantities() == null || request.getProductIds().isEmpty()) {
            throw new IllegalArgumentException("Products and quantities are required");
        }

        if (request.getProductIds().size() != request.getQuantities().size()) {
            throw new IllegalArgumentException("Products and quantities must match in size");
        }

        Long userId = getAuthenticatedUserId();

        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        Double total = 0.0;

        Set<Long> uniqueProducts = new HashSet<>(request.getProductIds());
        if (uniqueProducts.size() != request.getProductIds().size()) {
            throw new IllegalArgumentException("Each product ID can only appear once");
        }


        for (int i = 0; i < request.getProductIds().size(); i++) {
            Long productId = request.getProductIds().get(i);
            Integer quantity = request.getQuantities().get(i);

            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity for product ID " + productId + " must be greater than 0");
            }

            Product product = productService.getProductById(productId)
                    .orElseThrow(ProductNotFoundException::new);

            stockService.changeStock(productId, -quantity);

            double subtotal = product.getPrice() * quantity * (1 - product.getDiscount());
            total += subtotal;

            OrderItem item = new OrderItem();
            item.setOrder(order); 
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setSubtotal(subtotal);

            orderItems.add(item);
        }

        order.setProducts(orderItems);

        String couponCode = request.getCouponCode();

        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon coupon = couponService.findByCode(request.getCouponCode())
                    .orElseThrow(() -> new IllegalArgumentException("Coupon is invalid."));

            if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Coupon is expired.");
            }

            order.setCoupon(coupon);
            total -= total * coupon.getDiscount();
        }

        order.setTotalPrice(total);

        orderService.save(order);

        List<OrderItemResponse> itemResponses = order.getProducts().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new PurchaseResponse(
                order.getId(),
                userId,
                itemResponses,
                total,
                couponCode,
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