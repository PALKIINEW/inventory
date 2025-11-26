package com.store.inventory.controller;

import com.store.inventory.model.Product;
import com.store.inventory.model.ProductImage;
import com.store.inventory.service.ProductImageService;
import com.store.inventory.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService imageService;
    private final ProductService productService;

    public ProductImageController(ProductImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    // Upload image for a product
    @PostMapping("/upload/{productId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable String productId,
            @RequestParam("file") MultipartFile file) throws Exception {

        Product product = productService.getById(productId); // make sure this exists
        ProductImage savedImage = imageService.uploadImage(product, file);
        return ResponseEntity.ok(savedImage);
    }

    // Get all images for a product
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductImage>> getImages(@PathVariable String productId) throws Exception {
        Product product = productService.getById(productId);
        return ResponseEntity.ok(imageService.getImagesByProduct(product));
    }

    // Delete an image
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok("Deleted");
    }
}
