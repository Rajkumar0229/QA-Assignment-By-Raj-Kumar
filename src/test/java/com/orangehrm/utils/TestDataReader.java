package com.orangehrm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TestDataReader {
    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads test data from a JSON file and returns it as a Map
     * @param fileName Name of the JSON file (without extension)
     * @return Map containing the test data
     */
    public static Map<String, Object> getTestData(String fileName) {
        try {
            File file = new File(TEST_DATA_DIR + fileName + ".json");
            return objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data from " + fileName, e);
        }
    }

    /**
     * Reads test data from a JSON file and maps it to the specified class
     * @param fileName Name of the JSON file (without extension)
     * @param valueType Class to map the JSON data to
     * @return Object of the specified type containing the test data
     */
    public static <T> T getTestData(String fileName, Class<T> valueType) {
        try {
            File file = new File(TEST_DATA_DIR + fileName + ".json");
            return objectMapper.readValue(file, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data from " + fileName, e);
        }
    }
}
