package com.store.inventory.repository;

import com.store.inventory.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {

    // Find brand by name
    Optional<Brand> findByName(String name);

    // Check if brand exists by name
    boolean existsByName(String name);

    // Delete brand by name
    void deleteByName(String name);
}
