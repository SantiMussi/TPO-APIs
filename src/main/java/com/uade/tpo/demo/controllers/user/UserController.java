package com.uade.tpo.demo.controllers.user;

import com.uade.tpo.demo.controllers.order.OrderController;
import com.uade.tpo.demo.controllers.order.OrderItemResponse;
import com.uade.tpo.demo.controllers.product.ImageManager;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.demo.controllers.order.OrderDetailResponse;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.UserDuplicateException;
import com.uade.tpo.demo.service.OrderService;
import com.uade.tpo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private final PasswordEncoder passwordEncoder;

    @PutMapping("/{id}")
    public ResponseEntity<Object> changeUserInfo(@PathVariable Long id, @RequestBody UserChangeRequest request) {
        try {
            String pass = null;
            if (request.getPassword() != null) {
                pass = passwordEncoder.encode(request.getPassword());
            }
            User updatedUser = userService.changeUserInfo(id, request.getEmail(), request.getName(), pass,
                    request.getFirstName(), request.getLastName(), request.getRole());
            return ResponseEntity.ok(updatedUser);
        } catch (UserDuplicateException e) {
            return ResponseEntity.status(409).body("Email already exists.");
        }

    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok(userService.getUsers(PageRequest.of(0, Integer.MAX_VALUE)));

        return ResponseEntity.ok(userService.getUsers(PageRequest.of(page, size)));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserMe(
            @AuthenticationPrincipal com.uade.tpo.demo.entity.User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = (user.getRole() != null) ? user.getRole().name() : null;

        Map<String, Object> dto = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "role", role);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me/orders")
    public ResponseEntity<Page<OrderDetailResponse>> getUserOrders(@AuthenticationPrincipal User user) {
        try{
            List<Order> responses = orderService.getOrdersByUser(user.getId());

            LinkedList<OrderDetailResponse> orderDetailResponses = new LinkedList<>();

            for (Order order : responses) {

                LinkedList<OrderItemResponse> products = new LinkedList<>();

                for (OrderItem product : order.getProducts()) {
                    ProductResponse productResponse = new ProductResponse(
                            product.getProduct().getId(),
                            product.getProduct().getName(),
                            product.getProduct().getDescription(),
                            product.getProduct().getCategory().getId(),
                            product.getProduct().getCategory().getDescription(),
                            product.getProduct().getCreatorId(),
                            product.getProduct().getSize(),
                            product.getProduct().getStock(),
                            product.getProduct().getPrice(),
                            product.getProduct().getDiscount(),
                            ImageManager.fileToBase64(product.getProduct().getImg())
                    );

                    products.add(new OrderItemResponse(
                            productResponse,
                            product.getQuantity(),
                            product.getSubtotal()
                    ));
                }

                orderDetailResponses.add(new OrderDetailResponse(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        products));



            }


            return ResponseEntity.ok(new PageImpl<>(orderDetailResponses));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<Page<OrderDetailResponse>> getUserOrders(@PathVariable Long id) {
        try{
            List<Order> responses = orderService.getOrdersByUser(id);

            LinkedList<OrderDetailResponse> orderDetailResponses = new LinkedList<>();

            for (Order order : responses) {

                LinkedList<OrderItemResponse> products = new LinkedList<>();

                for (OrderItem product : order.getProducts()) {
                    ProductResponse productResponse = new ProductResponse(
                            product.getProduct().getId(),
                            product.getProduct().getName(),
                            product.getProduct().getDescription(),
                            product.getProduct().getCategory().getId(),
                            product.getProduct().getCategory().getDescription(),
                            product.getProduct().getCreatorId(),
                            product.getProduct().getSize(),
                            product.getProduct().getStock(),
                            product.getProduct().getPrice(),
                            product.getProduct().getDiscount(),
                            ImageManager.fileToBase64(product.getProduct().getImg())
                    );

                    products.add(new OrderItemResponse(
                            productResponse,
                            product.getQuantity(),
                            product.getSubtotal()
                    ));
                }

                orderDetailResponses.add(new OrderDetailResponse(
                        order.getId(),
                        order.getUser().getId(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        products));



            }


            return ResponseEntity.ok(new PageImpl<>(orderDetailResponses));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}