# OrangeHRM Test Automation Framework

This is a Selenium WebDriver based test automation framework for testing the OrangeHRM application. The framework is built using Java, TestNG, and follows the Page Object Model (POM) design pattern.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Framework Features](#framework-features)
- [Test Cases](#test-cases)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Prerequisites

Before you begin, ensure you have the following installed:

- Java JDK 11 or higher
- Maven 3.6.0 or higher
- Chrome/Firefox browser installed
- Git (for cloning the repository)
- Internet connection (for downloading dependencies)

## Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd Automation
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Configure test data**:
   - Update test data in the `src/test/resources/testdata/` directory
   - Modify configuration in `src/test/resources/config.properties`

## Project Structure

```
src/test/
├── java/
│   └── com/orangehrm/
│       ├── pages/           # Page Object classes
│       ├── tests/           # Test classes
│       ├── utils/           # Utility classes
│       ├── listeners/       # Test listeners
│       └── config/          # Configuration classes
└── resources/
    ├── testdata/           # Test data files (JSON)
    └── config.properties    # Configuration properties
```

## Configuration

Edit `src/test/resources/config.properties` to configure:

```properties
# Application URL
base.url=https://opensource-demo.orangehrmlive.com/

# Test Data
valid.username=Admin
valid.password=admin123

# Browser Configuration
browser=chrome
headless=false

# Timeouts
implicit.wait=10
page.load.timeout=30

# Report Configuration
report.path=test-output/ExtentReport.html
screenshot.path=test-output/screenshots/
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run specific test group
```bash
mvn test -Dgroups=smoke
```

### Run tests in parallel
```bash
mvn test -Dparallel=methods -DthreadCount=3
```

### Run with different browser
```bash
mvn test -Dbrowser=firefox
```

### Run in headless mode
```bash
mvn test -Dheadless=true
```

## Test Reports

Test execution reports are generated in the `test-output/` directory:

- `ExtentReport.html` - Detailed interactive HTML report with screenshots
- `emailable-report.html` - Email-friendly test report
- `screenshots/` - Directory containing screenshots of failed tests

## Framework Features

- **Page Object Model (POM)**: Clean separation of test logic and page-specific code
- **Data-Driven Testing**: Support for multiple test data sets using TestNG DataProvider
- **Cross-Browser Testing**: Run tests on Chrome, Firefox, and Edge
- **Parallel Execution**: Run tests in parallel for faster execution
- **Extent Reports**: Detailed HTML reports with screenshots
- **Automatic Screenshots**: Captures screenshots on test failure
- **Retry Mechanism**: Automatically retries failed tests
- **Configuration Management**: Externalized configuration properties
- **Logging**: Comprehensive logging for debugging

## Test Cases

### Login Module
1. Verify successful login with valid credentials
2. Verify login with invalid credentials
3. Verify login with empty credentials
4. Verify login with SQL injection attempt

### PIM Module
1. Verify PIM page is accessible
2. Add new employee with valid data
3. Add multiple employees using data provider
4. Verify required fields validation
5. Search for existing employee

### Admin Module
1. Verify Admin page is accessible
2. Add new system user
3. Add multiple users using data provider
4. Verify duplicate username validation
5. Search for existing user

## Troubleshooting

### Common Issues

1. **Browser not found**
   - Ensure the browser is installed in the default location
   - Update WebDriverManager if using a non-standard browser location

2. **Element not found**
   - Check if the element locators are up to date
   - Add explicit waits for dynamic elements

3. **Test failures**
   - Check the Extent Report for detailed failure information
   - Review screenshots in the `test-output/screenshots/` directory

### Logs

- Check the console output for detailed execution logs
- Logs are also saved to `test-output/logs/`

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
