package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler.java
 * Provides utilities for file I/O operations (saving/loading data).
 * Used for persistent storage via flat files or future serialization.
 */
public class FileHandler {

    private FileHandler() {
        // Utility class – prevent instantiation
    }

    /**
     * Writes content to a file.
     *
     * @param filePath The path to the file.
     * @param content  The content to write.
     * @return true if successfully written.
     */
    public static boolean writeToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Create directories if needed
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads content from a file.
     *
     * @param filePath The path to the file.
     * @return The file content as a string, or empty string if not found.
     */
    public static String readFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return "";
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return "";
        }
    }

    /**
     * Reads lines from a file.
     *
     * @param filePath The path to the file.
     * @return List of lines from the file.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return lines;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading lines from file: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Appends content to a file.
     *
     * @param filePath The path to the file.
     * @param content  The content to append.
     * @return true if successfully appended.
     */
    public static boolean appendToFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Create directories if needed
            
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(content);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a file exists.
     *
     * @param filePath The path to the file.
     * @return true if file exists.
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Deletes a file.
     *
     * @param filePath The path to the file.
     * @return true if successfully deleted.
     */
    public static boolean deleteFile(String filePath) {
        return new File(filePath).delete();
    }
}
