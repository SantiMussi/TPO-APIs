package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.demo.exceptions.CategoryHasProductException;
import com.uade.tpo.demo.exceptions.CategoryNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }




    //Si se lanza cualquier excepcion, se hace rollback
    @Transactional(rollbackFor = Throwable.class)
    public Category createCategory(String description) throws CategoryDuplicateException {



        List<Category> categories = categoryRepository.findByDescription(description);
        if (categories.isEmpty()) {
            return categoryRepository.save(new Category(description));
        }

        throw new CategoryDuplicateException();
    }

    public Optional<List<Product>> getProductsBySize(String size, long categoryId){
        return categoryRepository.findBySize(size, categoryId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Category deleteCategory(long id) throws CategoryNotFound, CategoryHasProductException {
        // System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        Optional<Category> cat = categoryRepository.findById(id);
        if (cat.isPresent()){
            // System.out.println(cat.get().getId());;
            if (cat.get().getProduct() != null && !cat.get().getProduct().isEmpty()) {
                throw new CategoryHasProductException();
            }
            categoryRepository.delete(cat.get());
            return cat.get();
        } else {
            throw new CategoryNotFound();
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public Category modifyCategory(Long id, String newDescription) throws CategoryNotFound, CategoryDuplicateException {

        Category category = categoryRepository.findById(id)
            .orElseThrow(CategoryNotFound::new);

        List<Category> existingCategories = categoryRepository.findByDescription(newDescription);
        if (!existingCategories.isEmpty() && !existingCategories.get(0).getId().equals(id)) {
            throw new CategoryDuplicateException();
        }

        category.setDescription(newDescription);

        return categoryRepository.save(category);
}
}
