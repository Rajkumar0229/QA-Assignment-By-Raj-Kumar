package com.orangehrm.utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
    
    @DataProvider(name = "loginCredentials")
    public static Object[][] getLoginCredentials() {
        return new Object[][] {
            // Valid credentials
            {ConfigReader.getUsername(), ConfigReader.getPassword(), true},
            // Invalid username
            {"invaliduser", ConfigReader.getPassword(), false},
            // Invalid password
            {ConfigReader.getUsername(), "invalidpass", false},
            // Empty credentials
            {"", "", false}
        };
    }
    
    @DataProvider(name = "employeeData")
    public static Object[][] getEmployeeData() {
        return new Object[][] {
            {"John", "Middle", "Doe"},
            {"Jane", "Ann", "Smith"},
            {"Robert", "James", "Johnson"}
        };
    }
    
    @DataProvider(name = "userRoleData")
    public static Object[][] getUserRoleData() {
        return new Object[][] {
            {"Admin", "Enabled"},
            {"ESS", "Enabled"},
            {"ESS", "Disabled"}
        };
    }
    
    @DataProvider(name = "adminUserData")
    public static Object[][] getAdminUserData() {
        String username = TestDataGenerator.generateRandomUsername();
        String password = TestDataGenerator.generateRandomPassword();
        String employeeName = TestDataGenerator.generateRandomName();
        
        return new Object[][] {
            {"Admin", employeeName, "Enabled", username, password}
        };
    }
}
