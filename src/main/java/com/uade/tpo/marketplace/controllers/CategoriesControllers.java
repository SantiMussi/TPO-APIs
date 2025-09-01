package com.uade.tpo.marketplace.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.service.CategoryService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@RestController
@RequestMapping("categories") //localhost:4002/categories
public class CategoriesControllers {

    @GetMapping //localhost:4002/categories
    public ArrayList<Category> getCategories() {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }

    @GetMapping("{categoryId}") //localhost:4002/categories/{id}
    public Category getCategoryById(@PathVariable int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping()
    public String createCategory(@RequestBody String categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoryId);
    }
    
}
