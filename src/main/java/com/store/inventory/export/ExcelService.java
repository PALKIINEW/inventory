package com.store.inventory.export;

import com.store.inventory.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class ExcelService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public byte[] productsToExcel(List<Product> products, int page, int size) throws IOException {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Products");
            int rowIdx = 0;

            // Header
            Row header = sheet.createRow(rowIdx++);
            String[] cols = {"#", "Name", "SKU", "Buying Price", "Selling Price", "Quantity", "Profit", "Available", "Expiration Date", "Barcode"};
            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            // Rows
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                Row row = sheet.createRow(rowIdx++);

                int serialNumber = page * size + i + 1;
                row.createCell(0).setCellValue(serialNumber);
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getSku() == null ? "" : p.getSku());
                row.createCell(3).setCellValue(p.getBuyingPrice());
                row.createCell(4).setCellValue(p.getSellingPrice());
                row.createCell(5).setCellValue(p.getQuantity());
                row.createCell(6).setCellValue(p.getSellingPrice() - p.getBuyingPrice());
                row.createCell(7).setCellValue(p.isAvailable());
                row.createCell(8).setCellValue(p.getExpirationDate() == null ? "" : p.getExpirationDate().format(DATE_FMT));

                // Barcode image
                if (p.getBarcodeImage() != null && !p.getBarcodeImage().isEmpty()) {
                    byte[] imgBytes = Base64.getDecoder().decode(p.getBarcodeImage());
                    int pictureIdx = wb.addPicture(imgBytes, Workbook.PICTURE_TYPE_PNG);
                    CreationHelper helper = wb.getCreationHelper();
                    Drawing<?> drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(9);
                    anchor.setRow1(row.getRowNum());
                    anchor.setCol2(10);
                    anchor.setRow2(row.getRowNum() + 1);
                    drawing.createPicture(anchor, pictureIdx);
                }
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }
}
