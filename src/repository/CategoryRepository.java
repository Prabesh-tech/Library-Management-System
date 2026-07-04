package repository;

import util.DataBaseConnection;
import java.sql.*;
import java.util.*;

/**
 * CategoryRepository.java
 * Data access layer for Category entities.
 * Interacts with the categories table in the database.
 * Falls back to in-memory storage when database is unavailable.
 */
public class CategoryRepository {

    private Connection conn;
    private Map<String, Integer> inMemoryCategories; // Fallback for demo mode
    private boolean isDatabaseAvailable;

    public CategoryRepository() {
        this.conn = DataBaseConnection.getConnection();
        this.inMemoryCategories = new LinkedHashMap<>();
        this.isDatabaseAvailable = (conn != null);
        
        // Initialize demo categories if database is not available
        if (!isDatabaseAvailable) {
            initializeDemoCategories();
        }
    }

    /**
     * Initialize demo categories for in-memory mode.
     */
    private void initializeDemoCategories() {
        inMemoryCategories.put("Programming", 3);
        inMemoryCategories.put("Web Development", 1);
        inMemoryCategories.put("Database", 1);
        inMemoryCategories.put("Technology", 1);
        inMemoryCategories.put("Computer Science", 2);
    }

    /**
     * Adds a new category to the database or in-memory store.
     *
     * @param name The category name
     * @param description The category description (optional)
     * @return true if successfully added, false otherwise
     */
    public boolean addCategory(String name, String description) {
        if (isDatabaseAvailable) {
            String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                ps.setString(2, description != null ? description : "");
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Error adding category: " + e.getMessage());
                return false;
            }
        } else {
            // In-memory mode
            if (inMemoryCategories.containsKey(name.trim())) {
                return false; // Already exists
            }
            inMemoryCategories.put(name.trim(), 0);
            return true;
        }
    }

    /**
     * Retrieves all categories with their book counts.
     *
     * @return A LinkedHashMap of category names to book counts
     */
    public LinkedHashMap<String, Integer> getAllCategoriesWithCounts() {
        if (isDatabaseAvailable) {
            LinkedHashMap<String, Integer> categories = new LinkedHashMap<>();
            String sql = "SELECT c.id, c.name, COUNT(b.id) as book_count " +
                         "FROM categories c " +
                         "LEFT JOIN books b ON c.id = b.category_id " +
                         "GROUP BY c.id, c.name " +
                         "ORDER BY c.name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String categoryName = rs.getString("name");
                    int bookCount = rs.getInt("book_count");
                    categories.put(categoryName, bookCount);
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving categories: " + e.getMessage());
            }
            return categories;
        } else {
            // Return in-memory categories
            return new LinkedHashMap<>(inMemoryCategories);
        }
    }

    /**
     * Gets a category by name.
     *
     * @param name The category name
     * @return The category ID, or -1 if not found
     */
    public int getCategoryByName(String name) {
        if (isDatabaseAvailable) {
            String sql = "SELECT id FROM categories WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error getting category: " + e.getMessage());
            }
            return -1;
        } else {
            // In-memory mode
            return inMemoryCategories.containsKey(name.trim()) ? 1 : -1;
        }
    }

    /**
     * Deletes a category by name.
     *
     * @param name The category name
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteCategory(String name) {
        if (isDatabaseAvailable) {
            String sql = "DELETE FROM categories WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                int affectedRows = ps.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Error deleting category: " + e.getMessage());
                return false;
            }
        } else {
            // In-memory mode
            return inMemoryCategories.remove(name.trim()) != null;
        }
    }

    /**
     * Gets the count of books in a category.
     *
     * @param categoryName The category name
     * @return The book count
     */
    public int getBookCountForCategory(String categoryName) {
        if (isDatabaseAvailable) {
            String sql = "SELECT COUNT(*) as count FROM books WHERE category_id = (SELECT id FROM categories WHERE name = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, categoryName.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("count");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error getting book count: " + e.getMessage());
            }
            return 0;
        } else {
            // In-memory mode
            return inMemoryCategories.getOrDefault(categoryName.trim(), 0);
        }
    }
}
