package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.ScreenshotUtil;
import com.orangehrm.utils.TestDataGenerator;
import com.orangehrm.utils.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PIMTest extends BaseTest {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private PIMPage pimPage;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
        dashboardPage = new DashboardPage(driver);
        dashboardPage.clickPIM();
        pimPage = new PIMPage(driver);
    }

    @Test(testName = "Verify PIM page is accessible")
    public void testPIMPageAccess() {
        Assert.assertTrue(pimPage.isPIMPageDisplayed(), "PIM page should be displayed");
    }

    @Test(testName = "Add new employee with random data")
    public void testAddNewEmployee() {
        String firstName = TestDataGenerator.generateRandomName().split(" ")[0];
        String middleName = "Middle";
        String lastName = TestDataGenerator.generateRandomName().split(" ")[1];
        
        pimPage.clickAddButton();
        pimPage.enterEmployeeDetails(firstName, middleName, lastName);
        pimPage.saveEmployee();
        
        String successMessage = pimPage.getSuccessMessage();
        // Check for either success message variant
        boolean isSuccess = successMessage.contains("Successfully Saved") || 
                          successMessage.contains("Successfully Updated") ||
                          successMessage.contains("Success");
        
        Assert.assertTrue(isSuccess, 
            "Employee should be added successfully. Actual message: " + successMessage);
    }
    
    @Test(testName = "Add multiple employees with data provider", dataProvider = "employeeData", dataProviderClass = TestDataProvider.class)
    public void testAddMultipleEmployees(String firstName, String middleName, String lastName) {
        try {
            pimPage.clickAddButton();
            pimPage.enterEmployeeDetails(firstName, middleName, lastName);
            pimPage.saveEmployee();
            
            String successMessage = pimPage.getSuccessMessage();
            // Check for either success message variant
            boolean isSuccess = successMessage.contains("Successfully Saved") || 
                              successMessage.contains("Successfully Updated") ||
                              successMessage.contains("Success");
            
            Assert.assertTrue(isSuccess, 
                "Employee should be added successfully. Actual message: " + successMessage);
                
            // Navigate back to PIM page for next test
            dashboardPage.clickPIM();
        } catch (Exception e) {
            // Take screenshot on failure
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "testAddMultipleEmployees_" + firstName + "_" + lastName);
            System.out.println("Screenshot saved at: " + screenshotPath);
            throw e;
        }
    }

    @Test(testName = "Verify required fields in Add Employee form")
    public void testRequiredFieldsInAddEmployee() {
        pimPage.clickAddButton();
        pimPage.saveEmployee();
        
        // Verify that the form shows validation errors for required fields
        try {
            // Check for validation error messages
            boolean hasValidationError = driver.getPageSource().contains("Required") || 
                                      driver.getPageSource().contains("required");
            Assert.assertTrue(hasValidationError, "Validation errors should be displayed for required fields");
        } catch (Exception e) {
            Assert.fail("Error while validating required fields: " + e.getMessage());
        }
        
        // Navigate back to PIM page for next test
        driver.navigate().back();
    }
}
