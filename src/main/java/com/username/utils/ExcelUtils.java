package com.username.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads an Excel sheet into a list of rows. Row 1 holds the column names;
 * each later row becomes a Map from column name to cell text.
 */
public final class ExcelUtils {

    private ExcelUtils() {
    }

    public static List<Map<String, String>> readSheet(String classpathFile, String sheetName) {
        try (InputStream in = ExcelUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("Excel file not found: " + classpathFile);
            }
            try (Workbook workbook = WorkbookFactory.create(in)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("No sheet named '" + sheetName + "' in " + classpathFile);
                }
                // DataFormatter returns every cell as the text you see in Excel (numbers, dates too)
                DataFormatter formatter = new DataFormatter();
                Row header = sheet.getRow(0);
                List<Map<String, String>> rows = new ArrayList<>();
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    Map<String, String> data = new LinkedHashMap<>();
                    for (int c = 0; c < header.getLastCellNum(); c++) {
                        data.put(formatter.formatCellValue(header.getCell(c)),
                                formatter.formatCellValue(row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
                    }
                    rows.add(data);
                }
                return rows;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + classpathFile, e);
        }
    }
}
