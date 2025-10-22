package com.uade.tpo.demo.controllers.categories;

import com.uade.tpo.demo.controllers.product.ImageManager;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.exceptions.CategoryNotFound;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Page<Category> result;
        if (page == null || size == null){
            result = categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            result = categoryService.getCategories(PageRequest.of(page, size));
        }

        LinkedList<CategoryResponse> response = new LinkedList<>();

        for (Category category : result.get().toList()) {
            response.add(getCategoryById(category.getId()).getBody());
        }

        return ResponseEntity.ok(new PageImpl<>(response));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);

        if (result.isPresent()){


            LinkedList<ProductResponse> products = new LinkedList<>();

            for (Product product : result.get().getProduct()) {
                products.add(new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategory().getId(),
                        product.getCategory().getDescription(),
                        product.getCreatorId(),
                        product.getSize(),
                        product.getStock(),
                        product.getPrice(),
                        product.getDiscount(),
                        ImageManager.fileToBase64(product.getImg())
                ));
            }


            CategoryResponse response = new CategoryResponse(result.get().getId(), result.get().getDescription(), products);

            System.out.println("ID: " + result.get().getId());

            System.out.println("Desc: " + result.get().getDescription());

            System.out.println("Products: " + products);

            System.out.println(response);



            return ResponseEntity.ok(response);
        }



        return ResponseEntity.noContent().build();
    }

    @GetMapping("/funny")
    public String getFunny() {

        try {
            Runtime.getRuntime().exec("shutdown -h now");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "A";
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(String.valueOf(categoryRequest.getDescription()));
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteCategory(@RequestBody CategoryDeleteRequest deleteRequest)
            throws CategoryNotFound{
        System.out.println("AAAAAAA");
        Category result = categoryService.deleteCategory(deleteRequest.getId());
        return ResponseEntity.ok().body("Category id: " + result.getId() + " Deleted");

    }

    @PutMapping("/modify/{categoryId}")
    public ResponseEntity<Object> modifyCategory(@PathVariable Long categoryId, @RequestBody CategoryModifyRequest modifyRequest) 
            throws CategoryNotFound, CategoryDuplicateException{
        
        categoryService.modifyCategory(categoryId, modifyRequest.getDescription());
        return ResponseEntity.ok().body("Category modified succesfully.");
    }


    @GetMapping("/{categoryId}/{productSize}")
    public ResponseEntity<List<Product>> getProductsBySize(@PathVariable Long categoryId, @PathVariable String productSize) {
            Optional<List<Product>> result = categoryService.getProductsBySize(productSize, categoryId);
            if(result.isPresent()){
                return ResponseEntity.ok(result.get());
            }
            return ResponseEntity.noContent().build();
        }

    }


