package com.store.inventory.service;

import com.store.inventory.model.Product;
import com.store.inventory.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;
    private final BarcodeService barcodeService;

    public ProductService(ProductRepository repo, BarcodeService barcodeService) {
        this.repo = repo;
        this.barcodeService = barcodeService;
    }

    // Create product with barcode generation
    public Product create(Product p) {
        p.updateAvailability();

        if (p.getSku() != null && !p.getSku().isEmpty()) {
            String barcode = barcodeService.generateBarcodeBase64(p.getSku());
            p.setBarcodeImage(barcode);
        }

        return repo.save(p);
    }

    public List<Product> all() {
        return repo.findAll();
    }

    public Optional<Product> findById(String id) {
        return repo.findById(id);
    }

    // Fixed getById method
    public Product getById(String id) throws Exception {
        return repo.findById(id)
                .orElseThrow(() -> new Exception("Product not found with id: " + id));
    }

    public Product update(String id, Product incoming) {
        return repo.findById(id).map(p -> {
            p.setName(incoming.getName());
            p.setSku(incoming.getSku());
            p.setBuyingPrice(incoming.getBuyingPrice());
            p.setSellingPrice(incoming.getSellingPrice());
            p.setQuantity(incoming.getQuantity());
            p.setExpirationDate(incoming.getExpirationDate());
            p.updateAvailability();

            // Regenerate barcode if SKU updated
            if (incoming.getSku() != null && !incoming.getSku().isEmpty()) {
                String barcode = barcodeService.generateBarcodeBase64(incoming.getSku());
                p.setBarcodeImage(barcode);
            }

            return repo.save(p);
        }).orElse(null);
    }

    public Product adjustQuantity(String id, int delta) {
        return repo.findById(id).map(p -> {
            p.setQuantity(Math.max(p.getQuantity() + delta, 0));
            p.updateAvailability();
            return repo.save(p);
        }).orElse(null);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    // Search helpers
    public List<Product> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByAvailable(boolean available) {
        return repo.findByAvailable(available);
    }

    public List<Product> searchExpiresBefore(LocalDate date) {
        return repo.findByExpirationDateBefore(date);
    }

    // Sorting logic
    public List<Product> sortProducts(List<Product> list, String sort) {
        return switch (sort) {
            case "low-cost" -> list.stream().sorted(Comparator.comparing(Product::getBuyingPrice)).toList();
            case "high-cost" -> list.stream().sorted(Comparator.comparing(Product::getBuyingPrice).reversed()).toList();
            case "low-profit" -> list.stream().sorted(Comparator.comparing(p -> p.getSellingPrice() - p.getBuyingPrice())).toList();
            case "high-profit" -> list.stream().sorted(Comparator.comparing((Product p) -> p.getSellingPrice() - p.getBuyingPrice()).reversed()).toList();
            case "date-asc" -> list.stream().sorted(Comparator.comparing(Product::getExpirationDate, Comparator.nullsLast(Comparator.naturalOrder()))).toList();
            case "date-desc" -> list.stream().sorted(Comparator.comparing(Product::getExpirationDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).toList();
            case "qty-asc" -> list.stream().sorted(Comparator.comparing(Product::getQuantity)).toList();
            case "qty-desc" -> list.stream().sorted(Comparator.comparing(Product::getQuantity).reversed()).toList();
            default -> list;
        };
    }

    public List<Product> findAllSorted(String sortBy, String direction) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return repo.findAll(sort);
    }

    public boolean existsBySku(String sku) {
        return repo.existsBySku(sku);
    }
}
