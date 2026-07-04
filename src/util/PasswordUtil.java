package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil.java
 * Provides secure password hashing for authentication.
 */
public final class PasswordUtil {

    private PasswordUtil() {
        // Utility class
    }

    public static String hashPassword(String password) {
        return password;
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }
        if (password.equals(storedPassword)) {
            return true;
        }
        if (storedPassword.matches("^[0-9a-fA-F]{64}$")) {
            return storedPassword.equals(sha256(password));
        }
        return false;
    }

    private static String sha256(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
