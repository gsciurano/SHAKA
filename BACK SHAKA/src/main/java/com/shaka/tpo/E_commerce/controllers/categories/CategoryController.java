package com.shaka.tpo.E_commerce.controllers.categories;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shaka.tpo.E_commerce.entity.Category;
import com.shaka.tpo.E_commerce.repository.CategoryRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Category body) {
        if (categoryRepository.existsByDescriptionIgnoreCase(body.getDescription())) {
            return ResponseEntity.status(409).body("La categoria ya existe");
        }
        Category category = new Category();
        category.setDescription(body.getDescription());
        Category saved = categoryRepository.save(category);
        return ResponseEntity.created(URI.create("/categories/" + saved.getId())).body(saved);
    }

}