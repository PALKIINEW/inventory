package com.store.inventory.service;

import com.store.inventory.model.Brand;
import com.store.inventory.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    // =========================
    // GET ALL BRANDS
    // =========================
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    // =========================
    // GET BRAND BY ID
    // =========================
    public Brand getById(String id) {
        Optional<Brand> optional = brandRepository.findById(id);
        return optional.orElse(null);
    }

    // =========================
    // ADD NEW BRAND
    // =========================
    public Brand add(Brand brand) {
        return brandRepository.save(brand);
    }

    // =========================
    // UPDATE BRAND
    // =========================
    public Brand update(String id, Brand brand) {
        Optional<Brand> existing = brandRepository.findById(id);
        if(existing.isEmpty()) return null;

        Brand toUpdate = existing.get();
        if(brand.getName() != null) toUpdate.setName(brand.getName());

        return brandRepository.save(toUpdate);
    }

    // =========================
    // DELETE BRAND
    // =========================
    public boolean delete(String id) {
        Optional<Brand> existing = brandRepository.findById(id);
        if(existing.isEmpty()) return false;

        brandRepository.deleteById(id);
        return true;
    }

    // =========================
    // CHECK IF BRAND EXISTS BY NAME
    // =========================
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }
}
