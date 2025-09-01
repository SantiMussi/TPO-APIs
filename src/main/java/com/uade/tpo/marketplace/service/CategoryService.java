package com.uade.tpo.marketplace.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.repository.CategoryRepository;

public class CategoryService {

    public ArrayList<Category> getCategories() {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategories();
    }

    public Category getCategoryById(int categoryId) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategoryById(categoryId);
    }

    public String createCategory(String entity) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.createCategory(entity);
    }
    
}
