package com.store.inventory.repository;

import com.store.inventory.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    // Find category by name (unique check)
    Optional<Category> findByName(String name);

    // Check if category exists by name
    boolean existsByName(String name);

    // Delete category by name
    void deleteByName(String name);
}
