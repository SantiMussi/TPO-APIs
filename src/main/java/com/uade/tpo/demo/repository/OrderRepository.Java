package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<>{

    
    
}
