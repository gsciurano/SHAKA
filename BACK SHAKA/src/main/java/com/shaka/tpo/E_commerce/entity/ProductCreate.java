package com.shaka.tpo.E_commerce.entity;

import lombok.Data;

@Data
public class ProductCreate {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
}