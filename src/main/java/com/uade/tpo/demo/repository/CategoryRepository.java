package com.uade.tpo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c FROM Category c WHERE c.description = ?1")
    List<Category> findByDescription(String description);

    @Query(value = "SELECT p FROM Category c JOIN c.product p WHERE p.size = ?1 AND p.stock > 0 AND c.id = ?2")
    Optional<List<Product>> findBySize(String size, long categoryId);
}
