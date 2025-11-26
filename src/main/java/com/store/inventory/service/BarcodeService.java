package com.store.inventory.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class BarcodeService {

    /**
     * Generates a barcode image (CODE_128) in Base64 format.
     * @param text Text to encode (e.g., SKU)
     * @return Base64 string of PNG barcode image
     */
    public String generateBarcodeBase64(String text) {
        try {
            // Create barcode matrix
            BitMatrix matrix = new MultiFormatWriter().encode(
                    text, BarcodeFormat.CODE_128, 300, 100
            );

            // Convert matrix to image and then to Base64
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "png", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Barcode generation failed for: " + text, e);
        }
    }
}
