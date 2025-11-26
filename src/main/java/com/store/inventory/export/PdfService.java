package com.store.inventory.export;

import com.store.inventory.model.Product;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public byte[] productsToPdf(List<Product> products, int page, int size) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Product Inventory", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(12f);
            document.add(title);

            PdfPTable table = new PdfPTable(10); // 10 columns including Barcode
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 4f, 3f, 2f, 2f, 2f, 2f, 2f, 3f, 4f});

            Font head = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            String[] headers = {"#", "Name", "SKU", "Buying Price", "Selling Price", "Qty", "Profit", "Available", "Expiration", "Barcode"};
            for (String h : headers) {
                table.addCell(new PdfPCell(new Phrase(h, head)));
            }

            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);

                int serialNumber = page * size + i + 1;
                table.addCell(new Phrase(String.valueOf(serialNumber), normal));
                table.addCell(new Phrase(p.getName(), normal));
                table.addCell(new Phrase(p.getSku() == null ? "" : p.getSku(), normal));
                table.addCell(new Phrase(String.valueOf(p.getBuyingPrice()), normal));
                table.addCell(new Phrase(String.valueOf(p.getSellingPrice()), normal));
                table.addCell(new Phrase(String.valueOf(p.getQuantity()), normal));
                table.addCell(new Phrase(String.valueOf(p.getSellingPrice() - p.getBuyingPrice()), normal));
                table.addCell(new Phrase(String.valueOf(p.isAvailable()), normal));
                table.addCell(new Phrase(p.getExpirationDate() == null ? "" : p.getExpirationDate().format(DATE_FMT), normal));

                // Barcode image
                if (p.getBarcodeImage() != null && !p.getBarcodeImage().isEmpty()) {
                    byte[] imgBytes = Base64.getDecoder().decode(p.getBarcodeImage());
                    Image img = Image.getInstance(imgBytes);
                    img.scaleToFit(80f, 40f);
                    PdfPCell imgCell = new PdfPCell(img, false);
                    table.addCell(imgCell);
                } else {
                    table.addCell(new PdfPCell(new Phrase("No Barcode", normal)));
                }
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        }
    }
}
