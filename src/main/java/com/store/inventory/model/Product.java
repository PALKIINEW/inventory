package com.store.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Product() {}

    public Product(String id, String name, String sku, double buyingPrice, double sellingPrice,
                   int quantity, LocalDate expirationDate, boolean available) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.available = available;
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

    // convenience
    public void updateAvailability() {
        this.available = this.quantity > 0;
    }
}
