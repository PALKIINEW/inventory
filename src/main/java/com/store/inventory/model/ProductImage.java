package com.store.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "product_images")
public class ProductImage {

    @Id
    private String id;

    private String filename;          // Original file name
    private String contentType;       // image/png, image/jpeg
    private String base64Data;        // Base64 encoded image

    @DBRef
    private Product product;          // Associated product

    // Constructors
    public ProductImage() {}

    public ProductImage(String filename, String contentType, String base64Data, Product product) {
        this.filename = filename;
        this.contentType = contentType;
        this.base64Data = base64Data;
        this.product = product;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getBase64Data() { return base64Data; }
    public void setBase64Data(String base64Data) { this.base64Data = base64Data; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
