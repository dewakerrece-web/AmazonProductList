package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

    String filePath;

    public ExcelUtility(String filePath) {
        this.filePath = filePath;
    }

    public List<Object[]> readBrandData(String sheetName) {
        List<Object[]> brandData = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String brand = row.getCell(0).getStringCellValue().trim();
                    double price = row.getCell(1).getNumericCellValue();
                    brandData.add(new Object[] { brand, price });
                }
            }
            fis.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println("Error reading Excel: " + e.getMessage());
        }
        return brandData;
    }

    public void writeResult(String sheetName, List<String[]> data) {
        try {
            Workbook workbook;
            if (new java.io.File(filePath).exists()) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                fis.close();
            } else {
                workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Brand Name");
                header.createCell(1).setCellValue("Product Name");
                header.createCell(2).setCellValue("Expected Price");
                header.createCell(3).setCellValue("Actual Price");
                header.createCell(4).setCellValue("Status");
            }

            int rowNum = sheet.getLastRowNum() + 1;
            for (String[] rowData : data) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    if (rowData[i] != null) {
                        row.createCell(i).setCellValue(rowData[i]);
                    } else {
                        row.createCell(i).setCellValue("");
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println("Error writing Excel: " + e.getMessage());
        }
    }
}
