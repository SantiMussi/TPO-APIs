package com.uade.tpo.demo.controllers.product;


import com.uade.tpo.demo.controllers.categories.CategoryRequest;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Description;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    /*

        \    /      |       |)
         \/\/       |       |

    */

    @Autowired
    private ProductService productService;

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
                productRequest.getDiscount()
        );

        System.out.println(result.getId());

        return ResponseEntity.created(URI.create("/product/" + result.getId())).body(result);
    }




}
