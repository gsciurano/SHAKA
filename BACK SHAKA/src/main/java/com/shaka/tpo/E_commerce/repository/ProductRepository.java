package com.shaka.tpo.E_commerce.repository;

import com.shaka.tpo.E_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);
    boolean existsByNameIgnoreCase(String name);
    List<Product> findByStockGreaterThan(Integer stock);
}