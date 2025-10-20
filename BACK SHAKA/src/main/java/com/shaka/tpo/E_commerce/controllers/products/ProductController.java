package com.shaka.tpo.E_commerce.controllers.products;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaka.tpo.E_commerce.entity.Category;
import com.shaka.tpo.E_commerce.entity.Product;
import com.shaka.tpo.E_commerce.repository.CategoryRepository;
import com.shaka.tpo.E_commerce.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        if (categoryId != null) {
            return ResponseEntity.ok(productRepository.findByCategory_Id(categoryId));
        }
        return ResponseEntity.ok(productRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product body) {
        
        if (body.getName() == null || body.getName().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        }
        if (body.getCategory() == null || body.getCategory().getId() == null) {
            return ResponseEntity.badRequest().body("category.id es obligatorio");
        }

       
        if (productRepository.existsByNameIgnoreCase(body.getName())) {
            return ResponseEntity.status(409).body("El producto ya existe");
        }

       
        Category category = categoryRepository.findById(body.getCategory().getId())
                .orElse(null);
        if (category == null) {
            return ResponseEntity.status(404).body("Categoria no encontrada");
        }

     
        body.setId(null);
        body.setCategory(category);

        Product saved = productRepository.save(body);
        return ResponseEntity.created(URI.create("/products/" + saved.getId())).body(saved);
    }

    @GetMapping("/en-stock")
    public ResponseEntity<List<Product>> getEnStock() {
        return ResponseEntity.ok(productRepository.findByStockGreaterThan(0));
}
@PostMapping("/{id}/comprar")
    public ResponseEntity<?> comprarProducto(@PathVariable Long id, @RequestParam("cantidad") Integer cantidad) {
        return productRepository.findById(id).map(product -> {
            if (cantidad == null || cantidad <= 0) {
                return ResponseEntity.badRequest().body("Cantidad debe ser mayor a 0");
            } else if (product.getStock() < cantidad) {
                return ResponseEntity.status(409).body("No hay suficiente stock disponible");
            } else {
                product.setStock(product.getStock() - cantidad);
                productRepository.save(product);
                return ResponseEntity.ok("Compra realizada con exito");
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}