package com.username.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** Reads JSON test data straight into Java objects (POJOs) with Jackson. */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> List<T> readList(String classpathFile, Class<T> type) {
        try (InputStream in = JsonUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("JSON file not found: " + classpathFile);
            }
            CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, type);
            return MAPPER.readValue(in, listType);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + classpathFile, e);
        }
    }

    public static <T> T read(String classpathFile, Class<T> type) {
        try (InputStream in = JsonUtils.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (in == null) {
                throw new IllegalArgumentException("JSON file not found: " + classpathFile);
            }
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + classpathFile, e);
        }
    }
}
