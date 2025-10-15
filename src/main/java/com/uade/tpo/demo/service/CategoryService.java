package com.uade.tpo.demo.service;

import java.util.Optional;
import java.util.List;

import com.uade.tpo.demo.exceptions.CategoryNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;

public interface CategoryService {
    public Page<Category> getCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryById(Long categoryId);

    public Category createCategory(String description) throws CategoryDuplicateException;
    public Optional<List<Product>> getProductsBySize(String size, long categoryId);
    public Category deleteCategory(long id) throws CategoryNotFound;

}