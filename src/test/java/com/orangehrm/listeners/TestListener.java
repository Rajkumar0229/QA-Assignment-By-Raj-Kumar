package com.orangehrm.listeners;

import com.aventstack.extentreports.Status;
import com.orangehrm.tests.BaseTest;
import com.orangehrm.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener extends BaseTest implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Log test start in Extent Report
        test = extent.createTest(result.getMethod().getMethodName());
        test.log(Status.INFO, "Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log failure in Extent Report
        test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        
        // Capture screenshot on test failure
        try {
            WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Handle test that fails but is within success percentage
    }

    @Override
    public void onStart(ITestContext context) {
        // Setup code before test suite starts
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush Extent Report after test suite completes
        if (extent != null) {
            extent.flush();
        }
    }
}
