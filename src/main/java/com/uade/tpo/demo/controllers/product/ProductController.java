package com.uade.tpo.demo.controllers.product;

import com.uade.tpo.demo.controllers.stock.StockAdjustRequest;
import com.uade.tpo.demo.controllers.stock.StockResponse;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.service.ProductService;
import com.uade.tpo.demo.service.StockService;
import com.uade.tpo.demo.entity.Category;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size){
        if (page == null || size == null)
            return ResponseEntity.ok(productService.getProducts(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(productService.getProducts(PageRequest.of(page, size)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }     


    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) {
        Product result = productService.createProduct(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getSize(),
                productRequest.getStock(),
                productRequest.getPrice(),
                productRequest.getDiscount(),
                productRequest.getCategoryId()
        );

        System.out.println(result.getId());

        return ResponseEntity.created(URI.create("/product/" + result.getId())).body(result);
    }



    //STOCK

    //Method to get stock of a product by its ID
    // GET /product/{productId}/stock ---> Stock
    @GetMapping("/{productId}/stock")
    public ResponseEntity<StockResponse> getStock(
        @PathVariable Long productId){
            try{
                int stock = stockService.getStock(productId);
                return ResponseEntity.ok(new StockResponse(productId, stock));
            } catch (EntityNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
    }
    
    //Method to adjust stock (increase or decrease)
    // POST /product/{productId}/stock/adjust (body: {"quantity": -3} or "5" for restock )
    @PostMapping("/{productId}/stock/adjust")
    public ResponseEntity<StockResponse> adjustStock(
        @PathVariable Long productId,
        @RequestBody StockAdjustRequest request){
            try{
                int newStock = stockService.changeStock(productId, request.getQuantity());
                return ResponseEntity.ok(new StockResponse(productId, newStock));
            }catch (EntityNotFoundException e){
                return ResponseEntity.notFound().build();
            } catch (IllegalArgumentException e){ //Insufficient stock for decrease
                return ResponseEntity.badRequest().build();
        }
    }

}
