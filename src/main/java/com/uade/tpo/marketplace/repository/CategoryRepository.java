package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository {
    public ArrayList<Category> categories = new ArrayList<Category>(
        Arrays.asList(Category.builder().id(1).description("Pantalones").build(),
                      Category.builder().id(2).description("Buzos").build(),
                      Category.builder().id(3).description("Remeras").build())
    );

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public Category getCategoryById(@PathVariable int categoryId) {
        return null;
    }

    public String createCategory(@RequestBody String entity) {
        return null;
    }
}
