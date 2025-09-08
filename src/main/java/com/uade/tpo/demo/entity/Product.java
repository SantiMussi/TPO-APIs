package com.uade.tpo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    public Product() {
    }

    public Product(String name, String description, String size, int stock, double price, double discount) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.stock = stock;
        this.price = price;
        this.discount = discount;
    }

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String size;

    @Column
    private int stock;

    @Column
    private double price;

    @Column
    private double discount;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnore
    private Category category;
}
