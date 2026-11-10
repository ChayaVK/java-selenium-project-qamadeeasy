package com.username.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads a simple CSV file (comma separated, first line = column names) into rows.
 * Good for small data sets; values must not contain commas.
 */
public final class CsvUtils {

    private CsvUtils() {
    }

    public static List<Map<String, String>> read(String classpathFile) {
        try (InputStream in = CsvUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("CSV file not found: " + classpathFile);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String[] header = reader.readLine().split(",");
            List<Map<String, String>> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < header.length; i++) {
                    row.put(header[i].trim(), i < values.length ? values[i].trim() : "");
                }
                rows.add(row);
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + classpathFile, e);
        }
    }
}
