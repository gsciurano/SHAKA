package com.shaka.tpo.E_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shaka.tpo.E_commerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByDescriptionIgnoreCase(String description);
    boolean existsByDescriptionIgnoreCase(String description);
}