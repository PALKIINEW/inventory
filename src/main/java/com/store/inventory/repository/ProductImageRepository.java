package com.store.inventory.repository;

import com.store.inventory.model.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
    List<ProductImage> findByProductId(String productId);
}
