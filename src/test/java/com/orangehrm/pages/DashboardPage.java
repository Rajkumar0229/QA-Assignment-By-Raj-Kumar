package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.utils.ScreenshotUtil;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h6[text()='Dashboard']")
    private WebElement dashboardHeader;

    @FindBy(xpath = "//span[text()='PIM']/parent::a")
    private WebElement pimMenu;

    @FindBy(xpath = "//span[text()='Admin']/parent::a")
    private WebElement adminMenu;

    @FindBy(xpath = "//span[@class='oxd-userdropdown-tab']")
    private WebElement userDropdown;

    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutButton;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(dashboardHeader)).isDisplayed();
    }

    public void clickPIM() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
    }

    public void clickAdmin() {
        wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
    
    /**
     * Waits for the dashboard to be fully loaded with multiple checks
     */
    public void waitForDashboardToLoad() {
        try {
            // Wait for the page to be fully loaded
            wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            
            // Try multiple possible selectors for the dashboard header
            try {
                // Check for dashboard header with text 'Dashboard'
                wait.until(ExpectedConditions.visibilityOf(dashboardHeader));
            } catch (Exception e) {
                // Try alternative selectors if the first one fails
                try {
                    WebElement altHeader = driver.findElement(By.xpath("//h5[contains(@class,'oxd-topbar-header-title') and contains(.,'Dashboard')]"));
                    wait.until(ExpectedConditions.visibilityOf(altHeader));
                } catch (Exception ex) {
                    // If both selectors fail, try one more common pattern
                    WebElement pageTitle = driver.findElement(By.cssSelector("h6.oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module"));
                    wait.until(ExpectedConditions.visibilityOf(pageTitle));
                }
            }
            
            // Wait for the user dropdown to be clickable as an additional check
            try {
                wait.until(ExpectedConditions.elementToBeClickable(userDropdown));
            } catch (Exception e) {
                // If user dropdown check fails, log it but don't fail the test
                System.out.println("Warning: User dropdown not clickable - " + e.getMessage());
            }
            
            // Small delay to ensure all elements are loaded
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "dashboard_load_interrupted");
            System.out.println("Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Interrupted while waiting for dashboard to load", e);
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "dashboard_load_failed");
            System.out.println("Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Failed to wait for dashboard to load: " + e.getMessage(), e);
        }
    }
}
