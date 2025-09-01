package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository {
    public ArrayList<Category> categories;

    public CategoryRepository(){
        categories = new ArrayList<Category>(
        Arrays.asList(Category.builder().id(1).description("Pantalones").build(),
                      Category.builder().id(2).description("Buzos").build(),
                      Category.builder().id(3).description("Remeras").build()));
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public Optional<Category> getCategoryById(@PathVariable int categoryId) {
                return this.categories.stream().filter(m -> m.getId() == categoryId).findAny();
    }

    public Category createCategory(int newCategoryId, String description) {
        Category newCategory = Category.builder()
                .description(description)
                .id(newCategoryId).build();
        this.categories.add(newCategory);
        return newCategory;
    }
}
