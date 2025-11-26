package com.store.inventory.controller;

import com.store.inventory.export.ExcelService;
import com.store.inventory.export.PdfService;
import com.store.inventory.export.WordService;
import com.store.inventory.model.Product;
import com.store.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final ExcelService excelService;
    private final PdfService pdfService;
    private final WordService wordService;

    public ProductController(ProductService service, ExcelService excelService, PdfService pdfService, WordService wordService) {
        this.service = service;
        this.excelService = excelService;
        this.pdfService = pdfService;
        this.wordService = wordService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product p) {

        // ================================
        // ✅ NEW: SKU duplicate protection
        // ================================
        if (p.getSku() != null && service.existsBySku(p.getSku())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "SKU already exists");
            return ResponseEntity.badRequest().body(error);
        }

        p.updateAvailability();
        Product saved = service.create(p);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Product>> all() {
        return ResponseEntity.ok(service.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable String id,
            @Valid @RequestBody Product p) {

        Product updated = service.update(id, p);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/adjust")
    public ResponseEntity<Product> adjust(
            @PathVariable String id,
            @RequestParam int delta) {

        Product updated = service.adjustQuantity(id, delta);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===========================
    // SEARCH + SORT ENDPOINT
    // ===========================
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiresBefore,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String expirationFilter
    ) {
        List<Product> results;

        if (name != null && !name.isBlank()) {
            results = service.searchByName(name);
        } else if (available != null) {
            results = service.searchByAvailable(available);
        } else if (expiresBefore != null) {
            results = service.searchExpiresBefore(expiresBefore);
        } else {
            results = service.all();
        }

        if (sort != null && !sort.isBlank()) {
            results = service.sortProducts(results, sort);
        }

        LocalDate today = LocalDate.now();
        if ("expired".equals(expirationFilter)) {
            results = results.stream()
                    .filter(p -> p.getExpirationDate() != null && p.getExpirationDate().isBefore(today))
                    .toList();
        } else if ("expiring-soon".equals(expirationFilter)) {
            results = results.stream()
                    .filter(p -> p.getExpirationDate() != null &&
                            !p.getExpirationDate().isBefore(today) &&
                            !p.getExpirationDate().isAfter(today.plusDays(7)))
                    .toList();
        }

        int start = Math.min(page * size, results.size());
        int end = Math.min(start + size, results.size());
        List<Product> paged = results.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("products", paged);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalItems", results.size());
        response.put("totalPages", (int) Math.ceil((double) results.size() / size));

        return ResponseEntity.ok(response);
    }

    // ===========================
    // EXPORT EXCEL
    // ===========================
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        // Pass page = 0 and pageSize = 1000 (or whatever you want to export)
        byte[] bytes = excelService.productsToExcel(service.all(), 0, 1000);
        String filename = "products.xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                URLEncoder.encode(filename, StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
    // ===========================
    // EXPORT PDF
    // ===========================
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {
        byte[] bytes = pdfService.productsToPdf(service.all(), 0, 1000); // pass page=0, size=1000
        String filename = "products.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                URLEncoder.encode(filename, StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @GetMapping("/export/word")
    public ResponseEntity<byte[]> exportWord() throws Exception {
        byte[] bytes = wordService.productsToWord(service.all(), 0, 1000);
        String filename = "products.docx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                URLEncoder.encode(filename, StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(bytes);
    }

}
