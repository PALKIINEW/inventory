package com.store.inventory.export;

import com.store.inventory.model.Product;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class WordService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public byte[] productsToWord(List<Product> products, int page, int size) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Title
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = title.createRun();
            run.setText("Product Inventory");
            run.setBold(true);
            run.setFontSize(16);
            run.addBreak();

            // Table
            XWPFTable table = doc.createTable();
            table.setWidth("100%");

            // Header row
            XWPFTableRow headerRow = table.getRow(0); // first row created by default
            String[] headers = {"#", "Name", "SKU", "Buying Price", "Selling Price", "Qty", "Profit", "Available", "Expiration", "Barcode"};
            for (int i = 0; i < headers.length; i++) {
                if (i == 0) {
                    headerRow.getCell(0).setText(headers[0]);
                } else {
                    headerRow.addNewTableCell().setText(headers[i]);
                }
            }

            // Rows
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                XWPFTableRow row = table.createRow();
                int serialNumber = page * size + i + 1;

                row.getCell(0).setText(String.valueOf(serialNumber));
                row.getCell(1).setText(p.getName());
                row.getCell(2).setText(p.getSku() == null ? "" : p.getSku());
                row.getCell(3).setText(String.valueOf(p.getBuyingPrice()));
                row.getCell(4).setText(String.valueOf(p.getSellingPrice()));
                row.getCell(5).setText(String.valueOf(p.getQuantity()));
                row.getCell(6).setText(String.valueOf(p.getSellingPrice() - p.getBuyingPrice()));
                row.getCell(7).setText(String.valueOf(p.isAvailable()));
                row.getCell(8).setText(p.getExpirationDate() == null ? "" : p.getExpirationDate().format(DATE_FMT));

                // Barcode image
                XWPFTableCell barcodeCell = row.getCell(9);
                if (p.getBarcodeImage() != null && !p.getBarcodeImage().isEmpty()) {
                    byte[] imgBytes = Base64.getDecoder().decode(p.getBarcodeImage());
                    XWPFParagraph cellParagraph = barcodeCell.addParagraph();
                    XWPFRun imgRun = cellParagraph.createRun();
                    imgRun.addPicture(new java.io.ByteArrayInputStream(imgBytes),
                            XWPFDocument.PICTURE_TYPE_PNG,
                            p.getSku() + ".png",
                            Units.toEMU(80),
                            Units.toEMU(40));
                } else {
                    barcodeCell.setText("No Barcode");
                }
            }

            doc.write(out);
            return out.toByteArray();
        }
    }
}
