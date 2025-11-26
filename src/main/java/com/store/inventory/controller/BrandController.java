package com.store.inventory.controller;

import com.store.inventory.model.Brand;
import com.store.inventory.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    // =========================
    // GET ALL BRANDS
    // =========================
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAll();
        return ResponseEntity.ok(brands);
    }

    // =========================
    // GET BRAND BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable String id) {
        Brand brand = brandService.getById(id);
        if(brand == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(brand);
    }

    // =========================
    // ADD NEW BRAND
    // =========================
    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody Brand brand) {
        if(brand.getName() == null || brand.getName().isBlank())
            return ResponseEntity.badRequest().body("Brand name cannot be empty");

        if(brandService.existsByName(brand.getName()))
            return ResponseEntity.badRequest().body("Brand already exists");

        Brand saved = brandService.add(brand);
        return ResponseEntity.ok(saved);
    }

    // =========================
    // UPDATE BRAND
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable String id, @RequestBody Brand brand) {
        Brand updated = brandService.update(id, brand);
        if(updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // =========================
    // DELETE BRAND
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable String id) {
        boolean deleted = brandService.delete(id);
        if(!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Deleted successfully");
    }
}
