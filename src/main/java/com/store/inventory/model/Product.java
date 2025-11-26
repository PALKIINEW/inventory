package com.store.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.store.inventory.model.Category;
import com.store.inventory.model.Brand;

import java.time.LocalDate;

@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private String sku;               // optional: stock keeping unit
    private double buyingPrice;
    private double sellingPrice;
    private int quantity;
    private LocalDate expirationDate; // stores as ISO date
    private boolean available;
    private String barcodeImage;

    private Category category;
    private Brand brand;

    public Product() {}

    public Product(String id, String name, String sku, double buyingPrice, double sellingPrice,
                   int quantity, LocalDate expirationDate, boolean available,
                   Category category, Brand brand) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.available = available;
        this.category = category;
        this.brand = brand;
    }

    // ===== Getters and Setters =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public double getBuyingPrice() { return buyingPrice; }
    public void setBuyingPrice(double buyingPrice) { this.buyingPrice = buyingPrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getBarcodeImage() { return barcodeImage; }
    public void setBarcodeImage(String barcodeImage) { this.barcodeImage = barcodeImage; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    // convenience
    public void updateAvailability() {
        this.available = this.quantity > 0;
    }
}
