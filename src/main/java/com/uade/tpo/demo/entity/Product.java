package com.uade.tpo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    public Product() {
    }

    public Product(String name, String description, Size size, int stock, double price, double discount, byte[] img) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.stock = stock;
        this.price = price;
        this.discount = discount;
        this.img = img;
    }

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Column
    private int stock;

    @Column
    private long creatorId;

    @Column
    private double price;

    @Column
    private double discount;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnore
    private Category category;

    @Lob
    @Column(name="imgdata", columnDefinition = "MEDIUMBLOB")
    private byte[] img;
}
