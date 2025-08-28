package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(testName = "Verify login with various credentials", dataProvider = "loginCredentials", dataProviderClass = TestDataProvider.class)
    public void testLoginWithVariousCredentials(String username, String password, boolean isValid) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        
        if (isValid) {
            DashboardPage dashboardPage = new DashboardPage(driver);
            Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after successful login");
            // Logout for the next test
            dashboardPage.logout();
        } else {
            if (username.isEmpty() && password.isEmpty()) {
                Assert.assertTrue(loginPage.isLoginPageDisplayed(), "User should stay on login page with empty credentials");
            } else {
                String errorMessage = loginPage.getErrorMessage().toLowerCase();
                Assert.assertTrue(errorMessage.contains("invalid") || errorMessage.contains("required"), 
                    "Error message should indicate invalid login");
            }
        }
    }
    
    @Test(testName = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
        
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after successful login");
        
        // Logout after test
        dashboardPage.logout();
    }
}
