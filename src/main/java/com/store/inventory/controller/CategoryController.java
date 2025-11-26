package com.store.inventory.controller;

import com.store.inventory.model.Category;
import com.store.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // =========================
    // GET ALL CATEGORIES
    // =========================
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    // =========================
    // GET CATEGORY BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        Category category = categoryService.getById(id);
        if(category == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    // =========================
    // ADD NEW CATEGORY
    // =========================
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if(category.getName() == null || category.getName().isBlank())
            return ResponseEntity.badRequest().body("Category name cannot be empty");

        if(categoryService.existsByName(category.getName()))
            return ResponseEntity.badRequest().body("Category already exists");

        Category saved = categoryService.add(category);
        return ResponseEntity.ok(saved);
    }

    // =========================
    // UPDATE CATEGORY
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody Category category) {
        Category updated = categoryService.update(id, category);
        if(updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // =========================
    // DELETE CATEGORY
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        boolean deleted = categoryService.delete(id);
        if(!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Deleted successfully");
    }
}
