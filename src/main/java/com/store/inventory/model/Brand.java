package com.store.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class Brand {

    @Id
    private String id;

    private String name;

    // Constructors
    public Brand() {}

    public Brand(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // ToString for debugging/logging
    @Override
    public String toString() {
        return "Brand{id='" + id + "', name='" + name + "'}";
    }
}
