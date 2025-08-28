# Bug Report: OrangeHRM Application

## 1. Login Page - No Rate Limiting on Failed Login Attempts
- **Severity**: High
- **Priority**: High
- **Steps to Reproduce**:
  1. Navigate to the login page
  2. Enter invalid credentials multiple times (10+ attempts)
- **Expected Result**: Account should be locked or CAPTCHA should appear after several failed attempts
- **Actual Result**: User can attempt to log in indefinitely without any restrictions

## 2. Employee ID Field Allows Special Characters
- **Severity**: Medium
- **Priority**: Medium
- **Steps to Reproduce**:
  1. Go to PIM > Add Employee
  2. Enter special characters (e.g., !@#$) in the Employee ID field
- **Expected Result**: System should validate and reject special characters
- **Actual Result**: System accepts special characters in the Employee ID field

## 3. Missing Input Validation on Employee Name Fields
- **Severity**: Medium
- **Priority**: Medium
- **Steps to Reproduce**:
  1. Go to PIM > Add Employee
  2. Enter numeric values in the First Name/Last Name fields
- **Expected Result**: System should validate and reject numeric input in name fields
- **Actual Result**: System accepts numeric values in name fields

## 4. Session Timeout Not Enforced
- **Severity**: High
- **Priority**: High
- **Steps to Reproduce**:
  1. Log in to the application
  2. Leave the application idle for an extended period (beyond session timeout)
  3. Try to perform an action
- **Expected Result**: User should be logged out and redirected to login page
- **Actual Result**: Session remains active indefinitely

## 5. Password Field Shows Plain Text on Error
- **Severity**: Medium
- **Priority**: Medium
- **Steps to Reproduce**:
  1. Go to login page
  2. Enter valid username and invalid password
  3. Click login
  4. Click browser's back button
- **Expected Result**: Password field should be masked or cleared
- **Actual Result**: Password is displayed in plain text

## 6. Duplicate Username Validation Not Working
- **Severity**: High
- **Priority**: High
- **Steps to Reproduce**:
  1. Go to Admin > Users
  2. Add a new user with a username that already exists
- **Expected Result**: System should prevent creation of duplicate usernames
- **Actual Result**: System allows creation of users with duplicate usernames

## 7. Search Functionality Not Working with Special Characters
- **Severity**: Low
- **Priority**: Medium
- **Steps to Reproduce**:
  1. Go to PIM > Employee List
  2. Search for an employee using special characters in the search field
- **Expected Result**: System should handle special characters gracefully
- **Actual Result**: Search breaks or returns incorrect results

## 8. Date Picker Allows Future Dates for Date of Birth
- **Severity**: Low
- **Priority**: Low
- **Steps to Reproduce**:
  1. Go to PIM > Add Employee
  2. Select a future date in the Date of Birth field
- **Expected Result**: System should validate and prevent future dates for DOB
- **Actual Result**: System accepts future dates for Date of Birth

## 9. Missing Required Field Indicators
- **Severity**: Low
- **Priority**: Low
- **Steps to Reproduce**:
  1. Go to PIM > Add Employee
  2. Leave required fields blank and submit
- **Expected Result**: Clear indication of required fields with asterisks (*)
- **Actual Result**: No visual indication of required fields until after submission

## 10. Inconsistent Error Message Formatting
- **Severity**: Low
- **Priority**: Low
- **Steps to Reproduce**:
  1. Attempt various invalid operations (login, form submissions, etc.)
- **Expected Result**: Consistent error message formatting across the application
- **Actual Result**: Inconsistent styling and formatting of error messages

## Bug Reporting Guidelines
- **Severity Levels**:
  - High: Critical functionality not working, security issues
  - Medium: Major functionality affected but workaround exists
  - Low: Minor issues, cosmetic problems

- **Priority Levels**:
  - High: Must be fixed in the next release
  - Medium: Should be fixed in upcoming releases
  - Low: Can be fixed in future releases

## Notes
- All bugs were found in OrangeHRM version 4.8.0
- Test environment: Chrome 115.0, Windows 10
- Date of testing: August 28, 2025
