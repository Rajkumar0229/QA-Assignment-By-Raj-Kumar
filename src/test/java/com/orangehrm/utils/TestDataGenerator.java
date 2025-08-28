package com.orangehrm.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {
    
    public static String generateRandomUsername() {
        return "testuser_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String generateRandomPassword() {
        return "Test@" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "@test.com";
    }
    
    public static String generateRandomName() {
        String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "Robert", "Lisa"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
        
        String firstName = firstNames[ThreadLocalRandom.current().nextInt(firstNames.length)];
        String lastName = lastNames[ThreadLocalRandom.current().nextInt(lastNames.length)];
        
        return firstName + " " + lastName;
    }
    
    public static String generateRandomEmployeeId() {
        return String.valueOf(1000 + ThreadLocalRandom.current().nextInt(9000));
    }
}
