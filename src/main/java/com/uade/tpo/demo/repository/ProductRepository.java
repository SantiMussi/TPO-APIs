package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.stock FROM Product p WHERE p.id = ?1")
    int getStock(long productId);

    @Modifying
    @Query(value = "UPDATE Product p SET p.stock = p.stock + ?2 WHERE p.id = ?1 AND (p.stock + ?2) >= 0")
    int changeStock(long productId, int quantity); //Negativo descuenta, positivo repone el stock

    @Query(value = "SELECT CASE WHEN COUNT(p)>0 THEN TRUE ELSE FALSE END FROM Product p WHERE p.name = ?1 AND p.description = ?2 AND p.size = ?3 AND p.price = ?4")
    boolean existsDuplicate(String productName, String productDescription, String productSize, double price);
}
