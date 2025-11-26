package com.store.inventory.repository;

import com.store.inventory.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // --- Custom Query Methods ---
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByAvailable(boolean available);
    List<Product> findByExpirationDateBefore(LocalDate date);

    // --- SKU existence check ---
    boolean existsBySku(String sku);
}
