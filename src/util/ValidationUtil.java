package util;

import java.util.regex.Pattern;

/**
 * ValidationUtil.java
 * Provides input validation utilities for the system.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtil() {
        // Utility class – prevent instantiation
    }

    /**
     * Validates an email address format.
     *
     * @param email The email to validate.
     * @return true if valid email format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates a password strength.
     *
     * @param password The password to validate.
     * @return true if password meets minimum requirements.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }

    /**
     * Validates an ISBN format.
     *
     * @param isbn The ISBN to validate.
     * @return true if valid ISBN format.
     */
    public static boolean isValidISBN(String isbn) {
        return isbn != null && isbn.length() >= 10;
    }

    /**
     * Validates that a string is not empty.
     *
     * @param str The string to validate.
     * @return true if string is not null and not empty.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates that a number is positive.
     *
     * @param number The number to validate.
     * @return true if number is > 0.
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }

    /**
     * Validates that a number is non-negative.
     *
     * @param number The number to validate.
     * @return true if number is >= 0.
     */
    public static boolean isNonNegative(double number) {
        return number >= 0.0;
    }
}
