package com.orangehrm.tests;

import com.orangehrm.pages.AdminPage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.ScreenshotUtil;
import com.orangehrm.utils.TestDataGenerator;
import com.orangehrm.utils.TestDataProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.*;

public class AdminTest extends BaseTest {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private AdminPage adminPage;

    @BeforeMethod
    public void setupTest() {
        try {
            loginPage = new LoginPage(driver);
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            dashboardPage = new DashboardPage(driver);
            // Ensure we're on the dashboard before clicking Admin
            dashboardPage.waitForDashboardToLoad();
            dashboardPage.clickAdmin();
            adminPage = new AdminPage(driver);
            // Ensure the Admin page is fully loaded before proceeding with tests
            Assert.assertTrue(adminPage.isAdminPageDisplayed(), "Failed to load Admin page");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "setupTest_failure");
            System.out.println("Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Test setup failed: " + e.getMessage(), e);
        }
    }

    @Test(testName = "Verify Admin page is accessible")
    public void testAdminPageAccess() {
        Assert.assertTrue(adminPage.isAdminPageDisplayed(), "Admin page should be displayed");
    }

    @Test(priority = 2, description = "Add a new system user with random data")
    public void testAddNewSystemUser() {
        String username = null;
        try {
            // Generate test data
            username = TestDataGenerator.generateRandomUsername();
            String password = TestDataGenerator.generateRandomPassword();
            String employeeName = "John Smith";
            
            // Navigate to Add User form
            adminPage.clickAddButton();
            
            // Add new user with a small delay to ensure the form is ready
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Add the new user
            adminPage.addNewUser("Admin", employeeName, "Enabled", username, password);
            
            // Verify successful user addition by checking if we're back on the System Users page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            try {
                // Wait for either the System Users header or search button to be visible
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h5[contains(@class, 'oxd-topbar-header-title') and contains(., 'System Users')]")),
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(@class, 'oxd-button') and contains(., 'Search')]"))
                ));
                
                System.out.println("Successfully navigated to System Users page after adding user");
                
            } catch (Exception e) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "system_users_page_not_loaded");
                System.out.println("Screenshot saved at: " + screenshotPath);
                Assert.fail("Failed to verify navigation to System Users page after adding user");
            }
            
            // Log the test data for reference
            System.out.println("Created test user - Username: " + username + ", Password: " + password);
            
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "testAddNewSystemUser_failed");
            System.out.println("Test failed. Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        } finally {
            // Clean up: Delete the test user if it was created
            if (username != null) {
                try {
                    adminPage.deleteUser(username);
                    System.out.println("Cleaned up test user: " + username);
                } catch (Exception e) {
                    System.out.println("Failed to clean up test user " + username + ": " + e.getMessage());
                }
            }
        }
    }

    @Test(priority = 2, description = "Add system user with data provider", 
          dataProvider = "adminUserData", dataProviderClass = TestDataProvider.class)
    public void testAddSystemUserWithDataProvider(String userRole, String employeeName, String status, String username, String password) {
        try {
            adminPage.clickAddButton();
            
            // Add a small delay to ensure the form is ready
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Add the new user
            boolean userAdded = adminPage.addNewUser(userRole, employeeName, status, username, password);
            Assert.assertTrue(userAdded, "Failed to add new user with data provider");
            
            // Verify successful navigation back to System Users page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h5[contains(@class, 'oxd-topbar-header-title') and contains(., 'System Users')]")),
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(@class, 'oxd-button') and contains(., 'Search')]"))
                ));
                
                System.out.println("Successfully added user with data provider: " + username);
                
            } catch (Exception e) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "data_provider_user_add_failed");
                System.out.println("Screenshot saved at: " + screenshotPath);
                Assert.fail("Failed to verify user addition with data provider: " + e.getMessage());
            }
            
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "testAddSystemUserWithDataProvider_failed");
            System.out.println("Test failed. Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        } finally {
            // Navigate back to Admin page for next test
            try {
                dashboardPage.clickAdmin();
            } catch (Exception e) {
                System.out.println("Warning: Failed to navigate back to Admin page: " + e.getMessage());
            }
        }
    }

    @Test(priority = 3, description = "Verify validation for existing username")
    public void testAddUserWithExistingUsername() {
        String username = null;
        try {
            // First, add a user
            username = TestDataGenerator.generateRandomUsername();
            String password = TestDataGenerator.generateRandomPassword();
            
            // Add the first user
            dashboardPage.clickAdmin();
            adminPage.clickAddButton();
            
            // Add the first user and verify success
            boolean firstUserAdded = adminPage.addNewUser("ESS", "John Smith", "Enabled", username, password);
            Assert.assertTrue(firstUserAdded, "Failed to add first user with username: " + username);
            
            // Navigate back to add user page
            dashboardPage.clickAdmin();
            adminPage.clickAddButton();
            
            // Try to add another user with the same username
            boolean secondUserAdded = adminPage.addNewUser("ESS", "Jane Doe", "Enabled", username, "AnotherPass123");
            
            // The second attempt should fail
            Assert.assertFalse(secondUserAdded, "Should not be able to add a user with an existing username");
            
            // Verify that we're still on the form (not redirected)
            try {
                boolean stillOnForm = driver.findElement(By.xpath("//h6[text()='Add User']")).isDisplayed();
                Assert.assertTrue(stillOnForm, "Should still be on the add user form after duplicate username attempt");
            } catch (Exception e) {
                // If we're not on the form, check for error message in the page
                String pageSource = driver.getPageSource().toLowerCase();
                boolean hasError = pageSource.contains("already exists") || 
                                 pageSource.contains("duplicate") ||
                                 pageSource.contains("already taken");
                
                if (!hasError) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "no_duplicate_error_found");
                    Assert.fail("No duplicate username error found. Screenshot: " + screenshotPath);
                }
            }
            
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "testAddUserWithExistingUsername_failed");
            System.out.println("Test failed. Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Test failed: " + e.getMessage(), e);
        } finally {
            // Clean up: Delete the test user if it was created
            if (username != null) {
                try {
                    adminPage.deleteUser(username);
                    System.out.println("Cleaned up test user: " + username);
                } catch (Exception e) {
                    System.out.println("Failed to clean up test user " + username + ": " + e.getMessage());
                }
            }
        }
    }
}
