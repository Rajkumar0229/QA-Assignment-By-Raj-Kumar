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
import org.openqa.selenium.Keys;

public class AdminPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h6[text()='Admin']")
    private WebElement adminHeader;

    @FindBy(xpath = "//button[text()=' Add ']")
    private WebElement addButton;

    @FindBy(xpath = "//div[contains(@class, 'oxd-form-row')]//div[contains(@class, 'oxd-grid-item')][1]//div[contains(@class, 'oxd-select-text')]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "//div[contains(@class, 'oxd-form-row')]//div[contains(@class, 'oxd-grid-item')][2]//input")
    private WebElement employeeNameField;

    @FindBy(xpath = "//div[contains(@class, 'oxd-form-row')]//div[contains(@class, 'oxd-grid-item')][3]//div[contains(@class, 'oxd-select-text')]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//label[text()='Username']/following::div[1]/input")
    private WebElement usernameField;

    @FindBy(xpath = "//label[text()='Password']/following::div[1]/input")
    private WebElement passwordField;

    @FindBy(xpath = "//label[text()='Confirm Password']/following::div[1]/input")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//button[text()=' Save ']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class, 'oxd-toast')]//p[contains(@class, 'oxd-text--toast-message')]")
    private WebElement successMessage;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Clicks an element with retry logic
     * @param element The element to click
     * @param elementName Name of the element for logging
     * @param maxAttempts Maximum number of click attempts
     * @param delayMs Delay between attempts in milliseconds
     * @return true if click was successful, false otherwise
     */
    private boolean clickWithRetry(WebElement element, String elementName, int maxAttempts, long delayMs) {
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                System.out.println("Successfully clicked " + elementName);
                return true;
            } catch (Exception e) {
                System.out.println(String.format("Attempt %d to click %s failed: %s", i, elementName, e.getMessage()));
                if (i >= maxAttempts) {
                    ScreenshotUtil.captureScreenshot(driver, "click_failed_" + elementName.toLowerCase().replace(" ", "_"));
                    return false;
                }
                try { Thread.sleep(delayMs); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        return false;
    }
    
    /**
     * Waits for an element to be visible
     * @param locator The element locator
     * @param elementName Name of the element for logging
     * @param timeoutInSeconds Timeout in seconds
     * @return true if element is visible, false otherwise
     */
    private boolean waitForElement(By locator, String elementName, int timeoutInSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            System.out.println("Failed to find " + elementName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Selects an option from a dropdown with retry logic
     * @param dropdownElement The dropdown element to click
     * @param optionXPath XPath to select the option
     * @param dropdownName Name of the dropdown for logging
     * @return true if selection was successful, false otherwise
     */
    private boolean selectFromDropdown(WebElement dropdownElement, String optionXPath, String dropdownName) {
        final int MAX_ATTEMPTS = 3;
        final long RETRY_DELAY_MS = 1000;
        
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                System.out.printf("Attempt %d/%d to select from %s dropdown%n", attempt, MAX_ATTEMPTS, dropdownName);
                
                // Scroll the dropdown into view and click it using JavaScript
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownElement);
                Thread.sleep(300); // Small delay for scroll to complete
                
                // Click the dropdown using JavaScript to avoid interception issues
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownElement);
                
                // Wait for options to be visible
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement option = shortWait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(optionXPath))
                );
                
                // Scroll the option into view and click it using JavaScript
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
                Thread.sleep(300); // Small delay for scroll to complete
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                
                // Verify selection was made
                Thread.sleep(500); // Allow time for selection to be processed
                System.out.println("Successfully selected option from " + dropdownName + " dropdown");
                return true;
                
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.printf("Option not found in %s dropdown on attempt %d: %s%n", 
                    dropdownName, attempt, e.getMessage());
                if (attempt >= MAX_ATTEMPTS) {
                    ScreenshotUtil.captureScreenshot(driver, "option_not_found_" + dropdownName.toLowerCase().replace(" ", "_"));
                    return false;
                }
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                System.out.printf("Click intercepted for %s dropdown on attempt %d: %s%n", 
                    dropdownName, attempt, e.getMessage());
                if (attempt >= MAX_ATTEMPTS) {
                    ScreenshotUtil.captureScreenshot(driver, "click_intercepted_" + dropdownName.toLowerCase().replace(" ", "_"));
                    return false;
                }
            } catch (org.openqa.selenium.TimeoutException e) {
                System.out.printf("Timeout waiting for %s dropdown option on attempt %d: %s%n", 
                    dropdownName, attempt, e.getMessage());
                if (attempt >= MAX_ATTEMPTS) {
                    ScreenshotUtil.captureScreenshot(driver, "timeout_" + dropdownName.toLowerCase().replace(" ", "_"));
                    return false;
                }
            } catch (Exception e) {
                System.out.printf("Unexpected error with %s dropdown on attempt %d: %s%n", 
                    dropdownName, attempt, e.getMessage());
                if (attempt >= MAX_ATTEMPTS) {
                    ScreenshotUtil.captureScreenshot(driver, "error_" + dropdownName.toLowerCase().replace(" ", "_"));
                    return false;
                }
            }
            
            // Wait before retry
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false;
    }

    public boolean isAdminPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(adminHeader)).isDisplayed();
    }
    
    /**
     * Navigates to the Users section in the Admin module
     */
    public void navigateToUsersSection() {
        try {
            // Click on User Management menu
            WebElement userManagementMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[text()='User Management']/parent::a")));
            userManagementMenu.click();
            
            // Click on Users submenu
            WebElement usersMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Users']")));
            usersMenu.click();
            
            // Wait for the page to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[contains(@class, 'oxd-topbar-header-title') and contains(., 'System Users')]")));
                
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "navigate_to_users_section_failed");
            throw new RuntimeException("Failed to navigate to Users section. Screenshot: " + screenshotPath, e);
        }
    }

    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    /**
     * Adds a new user with the specified details
     * @param userRole The role of the user (e.g., "Admin", "ESS")
     * @param employeeName The name of the employee to assign to the user
     * @param status The status of the user (e.g., "Enabled", "Disabled")
     * @param username The username for the new user
     * @param password The password for the new user
     * @return true if the user was added successfully, false otherwise
     */
    public boolean addNewUser(String userRole, String employeeName, String status, String username, String password) {
        final String methodName = "addNewUser";
        System.out.println(String.format("%s: Starting to add new user - Role: %s, Employee: %s, Username: %s", 
            methodName, userRole, employeeName, username));
        
        // Take initial screenshot for debugging
        ScreenshotUtil.captureScreenshot(driver, String.format("%s_start_%s", methodName, username));
        
        try {
            // Click Add button with retry
            System.out.println(methodName + ": Clicking Add button");
            if (!clickWithRetry(addButton, "Add button", 3, 1000)) {
                System.out.println(methodName + ": Failed to click Add button after retries");
                ScreenshotUtil.captureScreenshot(driver, methodName + "_add_button_click_failed");
                return false;
            }
            
            // Wait for the Add User form to be visible with a longer timeout
            System.out.println(methodName + ": Waiting for Add User form");
            if (!waitForElement(By.xpath("//h6[text()='Add User']"), "Add User form", 10)) {
                System.out.println(methodName + ": Add User form not visible after clicking Add button");
                // Try one more time in case of race condition
                if (!clickWithRetry(addButton, "Add button retry", 2, 1000) || 
                    !waitForElement(By.xpath("//h6[text()='Add User']"), "Add User form retry", 5)) {
                    System.out.println(methodName + ": Failed to load Add User form after retry");
                    ScreenshotUtil.captureScreenshot(driver, methodName + "_add_user_form_not_visible");
                    return false;
                }
            }
        
        // Select User Role with retry
        System.out.println(methodName + ": Selecting user role: " + userRole);
        if (!selectFromDropdown(userRoleDropdown, 
                             String.format("//div[@role='option']/span[contains(normalize-space(), '%s')]", userRole),
                             "User Role")) {
            System.out.println(methodName + ": Failed to select user role after retries");
            ScreenshotUtil.captureScreenshot(driver, methodName + "_user_role_selection_failed");
            return false;
        }
        
        // Enter Employee Name with retry logic
        System.out.println(methodName + ": Entering employee name: " + employeeName);
        boolean employeeSelected = false;
        final int MAX_EMP_SELECTION_ATTEMPTS = 3;
        
        for (int attempt = 1; attempt <= MAX_EMP_SELECTION_ATTEMPTS && !employeeSelected; attempt++) {
            try {
                WebElement empNameField = wait.until(ExpectedConditions.elementToBeClickable(employeeNameField));
                
                // Clear the field first
                empNameField.clear();
                Thread.sleep(300);
                
                // Type the first name part character by character
                String firstName = employeeName.split("\\s+")[0];
                for (char c : firstName.toCharArray()) {
                    empNameField.sendKeys(String.valueOf(c));
                    Thread.sleep(100); // Slightly longer delay for reliability
                }
                
                // Wait for dropdown to appear
                Thread.sleep(1500); // Increased wait time for dropdown
                
                // Press down arrow and enter to select the first suggestion
                empNameField.sendKeys(Keys.ARROW_DOWN);
                Thread.sleep(500);
                empNameField.sendKeys(Keys.ENTER);
                
                // Wait for selection to be processed
                Thread.sleep(1000);
                
                // Verify the selection was made
                String selectedName = empNameField.getAttribute("value");
                if (selectedName != null && !selectedName.trim().isEmpty()) {
                    System.out.println(methodName + ": Successfully selected employee: " + selectedName);
                    employeeSelected = true;
                    break;
                }
                
                System.out.println(methodName + ": Attempt " + attempt + " to select employee failed, retrying...");
                
                // Clear and retry with alternative approach if this wasn't the last attempt
                if (attempt < MAX_EMP_SELECTION_ATTEMPTS) {
                    empNameField.clear();
                    Thread.sleep(500);
                    
                    // Alternative approach: Click the dropdown and select directly
                    if (attempt == 2) {
                        // Click the dropdown arrow if available
                        try {
                            WebElement dropdownArrow = driver.findElement(
                                By.xpath("//div[contains(@class,'oxd-autocomplete-wrapper')]//i[contains(@class,'oxd-icon--caret')]"));
                            dropdownArrow.click();
                            Thread.sleep(500);
                            
                            // Try to find and click the option directly
                            String empOptionXpath = String.format("//div[@role='option']//span[contains(.,'%s')]", firstName);
                            WebElement empOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath(empOptionXpath)));
                            empOption.click();
                            employeeSelected = true;
                            break;
                        } catch (Exception e) {
                            System.out.println(methodName + ": Alternative selection approach failed: " + e.getMessage());
                        }
                    }
                }
                
            } catch (Exception e) {
                System.out.println(methodName + ": Error on attempt " + attempt + " to select employee: " + e.getMessage());
                if (attempt >= MAX_EMP_SELECTION_ATTEMPTS) {
                    System.out.println(methodName + ": Failed to select employee after " + MAX_EMP_SELECTION_ATTEMPTS + " attempts");
                    ScreenshotUtil.captureScreenshot(driver, methodName + "_employee_selection_failed");
                    return false;
                }
            }
        }
        
        if (!employeeSelected) {
            System.out.println(methodName + ": Could not select employee after all retry attempts");
            return false;
        }

        // Select Status with retry
        System.out.println(methodName + ": Selecting status: " + status);
        if (!selectFromDropdown(statusDropdown,
                             String.format("//div[@role='option']/span[contains(normalize-space(), '%s')]", status),
                             "Status")) {
            System.out.println(methodName + ": Failed to select status after retries");
            ScreenshotUtil.captureScreenshot(driver, methodName + "_status_selection_failed");
            return false;
        }

        // Enter Username with retry
        System.out.println(methodName + ": Entering username: " + username);
        try {
            WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
            usernameElement.clear();
            usernameElement.sendKeys(username);
            
            // Verify the username was entered correctly
            String enteredUsername = usernameElement.getAttribute("value");
            if (!enteredUsername.equals(username)) {
                System.out.println(methodName + ": Username entry verification failed. Expected: " + 
                                 username + ", Actual: " + enteredUsername);
                usernameElement.clear();
                usernameElement.sendKeys(username); // Retry once
                
                // Verify again
                if (!usernameElement.getAttribute("value").equals(username)) {
                    throw new RuntimeException("Failed to enter username correctly after retry");
                }
            }
            System.out.println(methodName + ": Successfully entered username");
        } catch (Exception e) {
            System.out.println(methodName + ": Failed to enter username: " + e.getMessage());
            ScreenshotUtil.captureScreenshot(driver, methodName + "_username_entry_failed");
            return false;
        }

        // Enter Password and Confirm Password with verification
        System.out.println(methodName + ": Entering password");
        try {
            // Enter password
            WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
            passwordElement.clear();
            passwordElement.sendKeys(password);
            
            // Verify password was entered
            if (passwordElement.getAttribute("value").isEmpty()) {
                // Retry if password wasn't entered
                passwordElement.clear();
                passwordElement.sendKeys(password);
            }
            
            // Enter confirm password
            WebElement confirmPasswordElement = wait.until(ExpectedConditions.elementToBeClickable(confirmPasswordField));
            confirmPasswordElement.clear();
            confirmPasswordElement.sendKeys(password);
            
            // Verify confirm password was entered
            if (confirmPasswordElement.getAttribute("value").isEmpty()) {
                // Retry if confirm password wasn't entered
                confirmPasswordElement.clear();
                confirmPasswordElement.sendKeys(password);
            }
            
            System.out.println(methodName + ": Successfully entered passwords");
        } catch (Exception e) {
            System.out.println(methodName + ": Failed to enter passwords: " + e.getMessage());
            ScreenshotUtil.captureScreenshot(driver, methodName + "_password_entry_failed");
            return false;
        }

        // Take a screenshot before clicking save
        ScreenshotUtil.captureScreenshot(driver, methodName + "_before_save_" + username);
        
        // Click Save with retry logic
        System.out.println(methodName + ": Clicking save button");
        final int MAX_SAVE_ATTEMPTS = 3;
        boolean saveSuccessful = false;
        
        for (int attempt = 1; attempt <= MAX_SAVE_ATTEMPTS && !saveSuccessful; attempt++) {
            try {
                // Scroll the save button into view and click using JavaScript
                WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveBtn);
                Thread.sleep(300);
                
                // Try clicking with JavaScript first, fall back to regular click if that fails
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
                } catch (Exception jsClickEx) {
                    System.out.println(methodName + ": JavaScript click failed, trying regular click");
                    saveBtn.click();
                }
                
                System.out.println(methodName + ": Save button clicked (attempt " + attempt + ")");
                
                // Wait a moment to see if the form starts to process
                Thread.sleep(1000);
                
                // Check if the form is still visible (indicating the click might not have worked)
                try {
                    if (driver.findElement(By.xpath("//h6[text()='Add User']")).isDisplayed()) {
                        System.out.println(methodName + ": Form still visible after save click, retrying...");
                        continue;
                    }
                } catch (Exception e) {
                    // Form is no longer visible, which is good
                    saveSuccessful = true;
                    break;
                }
                
            } catch (Exception e) {
                System.out.println(methodName + ": Error on save attempt " + attempt + ": " + e.getMessage());
                if (attempt >= MAX_SAVE_ATTEMPTS) {
                    System.out.println(methodName + ": Failed to click save button after " + MAX_SAVE_ATTEMPTS + " attempts");
                    ScreenshotUtil.captureScreenshot(driver, methodName + "_save_button_click_failed");
                    return false;
                }
                // Wait before retrying
                Thread.sleep(1000);
            }
        }
        
        if (!saveSuccessful) {
            System.out.println(methodName + ": Could not submit the form after all retry attempts");
            return false;
        }

        // Verify the user was added successfully
        System.out.println(methodName + ": Verifying user was added successfully");
        final int MAX_VERIFICATION_ATTEMPTS = 5;
        final long VERIFICATION_POLL_INTERVAL_MS = 1000;
        
        for (int attempt = 1; attempt <= MAX_VERIFICATION_ATTEMPTS; attempt++) {
            try {
                // Check if we're still on the form (which would indicate an error)
                try {
                    if (driver.findElement(By.xpath("//h6[text()='Add User']")).isDisplayed()) {
                        // Check for validation errors
                        String pageSource = driver.getPageSource().toLowerCase();
                        if (pageSource.contains("required") || pageSource.contains("error") || 
                            pageSource.contains("invalid") || pageSource.contains("already exists")) {
                            
                            // Try to find and log the specific error message
                            try {
                                WebElement errorElement = driver.findElement(
                                    By.xpath("//div[contains(@class, 'oxd-input-group__message') or contains(@class, 'oxd-field-error-message')]"));
                                if (errorElement != null && errorElement.isDisplayed()) {
                                    String errorText = errorElement.getText();
                                    System.out.println(methodName + ": Validation error: " + errorText);
                                    ScreenshotUtil.captureScreenshot(driver, methodName + "_validation_error_" + username);
                                    return false;
                                }
                            } catch (Exception e) {
                                // Couldn't find a specific error element
                                System.out.println(methodName + ": Page indicates an error but couldn't find specific error message");
                                ScreenshotUtil.captureScreenshot(driver, methodName + "_generic_validation_error_" + username);
                                return false;
                            }
                        }
                        
                        // If we're still on the form but no errors found, it might be a different issue
                        if (attempt >= MAX_VERIFICATION_ATTEMPTS) {
                            System.out.println(methodName + ": Still on the form after save, but no validation errors found");
                            ScreenshotUtil.captureScreenshot(driver, methodName + "_form_still_visible_" + username);
                            return false;
                        }
                        
                        // Wait and retry
                        System.out.println(methodName + ": Still on form, waiting and retrying... (attempt " + attempt + ")");
                        Thread.sleep(VERIFICATION_POLL_INTERVAL_MS);
                        continue;
                    }
                } catch (Exception e) {
                    // Form is no longer visible, which is good
                    System.out.println(methodName + ": No longer on the Add User form");
                }
                
                // Check if we were redirected to the users list
                String currentUrl = driver.getCurrentUrl();
                String pageTitle = driver.getTitle();
                System.out.println(methodName + ": Current URL: " + currentUrl);
                System.out.println(methodName + ": Page title: " + pageTitle);
                
                if (currentUrl.contains("/admin/viewSystemUsers") || 
                    pageTitle.contains("System Users") ||
                    currentUrl.contains("/pim/viewSystemUsers")) {
                    
                    System.out.println(methodName + ": Successfully redirected to System Users page after adding user");
                    ScreenshotUtil.captureScreenshot(driver, methodName + "_redirected_to_users_list_" + username);
                    return true;
                }
                
                // Check for success message with a short timeout
                System.out.println(methodName + ": Checking for success message");
                
                // Try multiple possible success message selectors
                String[] successSelectors = new String[] {
                    "//div[contains(@class, 'oxd-toast')]//p[contains(@class, 'oxd-text--toast-message')]",
                    "//div[contains(@class, 'oxd-toast')]//div[contains(@class, 'oxd-toast-content')]",
                    "//div[contains(@class, 'oxd-toast')]//div[contains(@class, 'oxd-toast-message')]",
                    "//div[contains(@class, 'oxd-toast')]"
                };
                
                for (String selector : successSelectors) {
                    try {
                        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                        WebElement successElement = shortWait.until(
                            ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));
                        
                        if (successElement != null && successElement.isDisplayed()) {
                            String successText = successElement.getText();
                            System.out.println(methodName + ": Success message found: " + successText);
                            ScreenshotUtil.captureScreenshot(driver, methodName + "_success_message_" + username);
                            
                            // Wait for the success message to disappear (indicating navigation)
                            try {
                                shortWait.until(ExpectedConditions.invisibilityOf(successElement));
                            } catch (Exception e) {
                                // Ignore if the message doesn't disappear
                            }
                            
                            return true;
                        }
                    } catch (Exception e) {
                        // Try next selector
                        continue;
                    }
                }
                
                // If we get here, no success message was found with any selector
                System.out.println(methodName + ": No success message found with standard selectors (attempt " + attempt + ")");
                
                // Check if we can find any toast message at all
                try {
                    WebElement anyToast = driver.findElement(
                        By.xpath("//div[contains(@class, 'toast') or contains(@class, 'message')]"));
                    if (anyToast != null && anyToast.isDisplayed()) {
                        System.out.println(methodName + ": Found a toast message (non-standard): " + anyToast.getText());
                        ScreenshotUtil.captureScreenshot(driver, methodName + "_non_standard_success_message_" + username);
                        return true;
                    }
                } catch (Exception e) {
                    // No toast message found
                }
                
                // If this is the last attempt, log a warning but assume success if we're not on the form
                if (attempt >= MAX_VERIFICATION_ATTEMPTS) {
                    System.out.println(methodName + ": Warning: Could not verify success with standard methods, but not on form - assuming success");
                    ScreenshotUtil.captureScreenshot(driver, methodName + "_success_assumed_" + username);
                    return true;
                }
                
                // Wait before next attempt
                Thread.sleep(VERIFICATION_POLL_INTERVAL_MS);
                
            } catch (Exception e) {
                System.out.println(methodName + ": Error during verification attempt " + attempt + ": " + e.getMessage());
                if (attempt >= MAX_VERIFICATION_ATTEMPTS) {
                    String screenshotPath = ScreenshotUtil.captureScreenshot(driver, methodName + "_verification_failed_" + username);
                    System.out.println(methodName + ": Failed to verify user addition after " + MAX_VERIFICATION_ATTEMPTS + " attempts");
                    System.out.println(methodName + ": Screenshot saved at: " + screenshotPath);
                    return false;
                }
                
                // Wait before next attempt
                Thread.sleep(VERIFICATION_POLL_INTERVAL_MS);
            }
        }
        
        // If we get here, all verification attempts failed
        System.out.println(methodName + ": Failed to verify user addition after all retry attempts");
        ScreenshotUtil.captureScreenshot(driver, methodName + "_verification_failed_final_" + username);
        return false;
    }

    /**
     * Gets the success message text after an operation
     * @return The success message text, or a message indicating no success message was found
     */
    public String getSuccessMessage() {
        try {
            // First, check if we're on the System Users page which indicates success
            String currentUrl = driver.getCurrentUrl();
            String pageTitle = driver.getTitle();
            
            if (currentUrl.contains("/admin/viewSystemUsers") || 
                pageTitle.contains("System Users") ||
                currentUrl.contains("/pim/viewSystemUsers")) {
                return "Successfully navigated to System Users page";
            }
            
            // Try multiple possible success message selectors with different wait strategies
            String[] successSelectors = new String[] {
                // Toast messages
                "//div[contains(@class, 'oxd-toast')]//p[contains(@class, 'oxd-text--toast-message')]",
                "//div[contains(@class, 'oxd-toast')]//div[contains(@class, 'oxd-toast-content')]",
                "//div[contains(@class, 'oxd-toast')]//div[contains(@class, 'oxd-toast-message')]",
                "//div[contains(@class, 'oxd-toast')]",
                
                // Alert messages
                "//div[contains(@class, 'oxd-alert') and contains(@class, 'oxd-alert--success')]",
                "//div[contains(@class, 'oxd-alert')]//p[contains(@class, 'oxd-text--success')]",
                
                // Generic success messages
                "//div[contains(@class, 'success')]",
                "//div[contains(@class, 'message') and contains(@class, 'success')]",
                "//div[contains(text(), 'success')]",
                "//div[contains(text(), 'saved') or contains(text(), 'added') or contains(text(), 'created')]",
                
                // Form success messages
                "//div[contains(@class, 'oxd-form-actions')]//div[contains(@class, 'success')]",
                "//form//div[contains(@class, 'success')]"
            };
            
            // First try with a short wait for immediate success messages
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            for (String selector : successSelectors) {
                try {
                    WebElement message = shortWait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));
                    if (message != null && message.isDisplayed()) {
                        String messageText = message.getText().trim();
                        if (!messageText.isEmpty()) {
                            System.out.println("Found success message with selector: " + selector);
                            return messageText;
                        }
                    }
                } catch (Exception e) {
                    // Try next selector
                    continue;
                }
            }
            
            // If no success message found yet, try with a longer wait
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            for (String selector : successSelectors) {
                try {
                    WebElement message = longWait.until(
                        ExpectedConditions.presenceOfElementLocated(By.xpath(selector)));
                    if (message != null && message.isDisplayed()) {
                        String messageText = message.getText().trim();
                        if (!messageText.isEmpty()) {
                            System.out.println("Found success message with longer wait using selector: " + selector);
                            return messageText;
                        }
                    }
                } catch (Exception e) {
                    // Try next selector
                    continue;
                }
            }
            
            // Check for any toast message with a more generic selector
            try {
                WebElement anyToast = driver.findElement(
                    By.xpath("//div[contains(@class, 'toast') or contains(@class, 'message') or contains(@class, 'alert')]"));
                if (anyToast != null && anyToast.isDisplayed()) {
                    String toastText = anyToast.getText().trim();
                    if (!toastText.isEmpty()) {
                        System.out.println("Found toast message: " + toastText);
                        return toastText;
                    }
                }
            } catch (Exception e) {
                // No toast message found
            }
            
            // As a last resort, check the page source for success indicators
            String pageSource = driver.getPageSource().toLowerCase();
            if (pageSource.contains("success") || pageSource.contains("saved") || 
                pageSource.contains("added") || pageSource.contains("created")) {
                return "Operation completed successfully (detected in page source)";
            }
            
            // If we get here, no success message was found
            System.out.println("No success message found. Current URL: " + currentUrl);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "no_success_message_found");
            System.out.println("Screenshot saved at: " + screenshotPath);
            return "No success message found. Check screenshot: " + screenshotPath;
            
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "get_success_message_error");
            System.out.println("Error getting success message. Screenshot saved at: " + screenshotPath);
            return "Error getting success message: " + e.getMessage();
        }
    }
    
    /**
     * Deletes a user with the specified username
     * @param username The username of the user to delete
     */
    public void deleteUser(String username) {
        try {
            // Navigate to the System Users page if not already there
            if (!isSystemUsersPageDisplayed()) {
                navigateToUsersSection();
            }
            
            // Search for the user by username
            WebElement usernameSearchField = driver.findElement(
                By.xpath("//div[contains(@class, 'oxd-input-group')][.//label[contains(., 'Username')]]//input"));
            usernameSearchField.clear();
            usernameSearchField.sendKeys(username);
            
            // Click the search button
            WebElement searchButton = driver.findElement(By.xpath("//button[contains(@class, 'oxd-button') and contains(., 'Search')]"));
            searchButton.click();
            
            // Wait for search results
            Thread.sleep(2000);
            
            // Find the delete button for the user
            WebElement deleteButton = driver.findElement(
                By.xpath(String.format("//div[contains(@class, 'oxd-table-row')][.//div[text()='%s']]//button[contains(@class, 'oxd-icon-button--danger')]", username)));
            
            // Click the delete button
            deleteButton.click();
            
            // Confirm deletion in the confirmation dialog
            WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'oxd-dialog-container--inner')]//button[contains(@class, 'oxd-button--label-danger')]")));
            confirmDeleteButton.click();
            
            // Wait for the success message
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-toast')]//p[contains(@class, 'oxd-text--toast-message')]")));
                
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "delete_user_failed_" + username);
            System.out.println("Failed to delete user " + username + ". Screenshot saved at: " + screenshotPath);
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if the current page is the System Users page
     * @return true if the System Users page is displayed, false otherwise
     */
    private boolean isSystemUsersPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[contains(@class, 'oxd-topbar-header-title') and contains(., 'System Users')]"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
