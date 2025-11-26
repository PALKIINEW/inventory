package com.store.inventory.service;

import com.store.inventory.model.Product;
import com.store.inventory.model.ProductImage;
import com.store.inventory.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.io.IOException;

@Service
public class ProductImageService {

    private final ProductImageRepository repository;

    public ProductImageService(ProductImageRepository repository) {
        this.repository = repository;
    }

    public ProductImage uploadImage(Product product, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);

        ProductImage image = new ProductImage(file.getOriginalFilename(), file.getContentType(), base64, product);
        return repository.save(image);
    }

    public List<ProductImage> getImagesByProduct(Product product) {
        return repository.findByProductId(product.getId());
    }

    public void deleteImage(String imageId) {
        repository.deleteById(imageId);
    }
}
