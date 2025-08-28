package com.orangehrm.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.ScreenshotUtil;
import com.orangehrm.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setUpReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void setup(Method method) {
        test = extent.createTest(method.getName(), method.getAnnotation(Test.class).testName());
        driver = DriverManager.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.get(ConfigReader.getBaseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot on test failure
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            try {
                test.addScreenCaptureFromPath(screenshotPath);
                test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
            } catch (Exception e) {
                test.log(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test skipped: " + result.getName());
        } else {
            test.pass("Test passed");
        }
        
        // Quit the driver
DriverManager.quitDriver();
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
