package com.store.inventory.service;

import com.store.inventory.model.Category;
import com.store.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // =========================
    // GET ALL CATEGORIES
    // =========================
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // =========================
    // GET CATEGORY BY ID
    // =========================
    public Category getById(String id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.orElse(null);
    }

    // =========================
    // ADD NEW CATEGORY
    // =========================
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    // =========================
    // UPDATE CATEGORY
    // =========================
    public Category update(String id, Category category) {
        Optional<Category> existing = categoryRepository.findById(id);
        if(existing.isEmpty()) return null;

        Category toUpdate = existing.get();
        if(category.getName() != null) toUpdate.setName(category.getName());

        return categoryRepository.save(toUpdate);
    }

    // =========================
    // DELETE CATEGORY
    // =========================
    public boolean delete(String id) {
        Optional<Category> existing = categoryRepository.findById(id);
        if(existing.isEmpty()) return false;

        categoryRepository.deleteById(id);
        return true;
    }

    // =========================
    // CHECK IF CATEGORY EXISTS BY NAME
    // =========================
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
