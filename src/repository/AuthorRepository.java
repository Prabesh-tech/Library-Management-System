package repository;

import util.DataBaseConnection;
import java.sql.*;
import java.util.*;

/**
 * AuthorRepository.java
 * Data access layer for Author entities.
 * Interacts with the authors table in the database.
 * Falls back to in-memory storage when database is unavailable.
 */
public class AuthorRepository {

    private Connection conn;
    private Map<String, Integer> inMemoryAuthors; // Fallback for demo mode
    private boolean isDatabaseAvailable;

    public AuthorRepository() {
        this.conn = DataBaseConnection.getConnection();
        this.inMemoryAuthors = new LinkedHashMap<>();
        this.isDatabaseAvailable = (conn != null);
        
        // Initialize demo authors if database is not available
        if (!isDatabaseAvailable) {
            initializeDemoAuthors();
        }
    }

    /**
     * Initialize demo authors for in-memory mode.
     */
    private void initializeDemoAuthors() {
        inMemoryAuthors.put("David Thomas", 1);
        inMemoryAuthors.put("Gang of Four", 1);
        inMemoryAuthors.put("Robert C. Martin", 1);
        inMemoryAuthors.put("Paul Deitel", 1);
        inMemoryAuthors.put("Jennifer Widom", 1);
        inMemoryAuthors.put("Stuart Russell", 1);
        inMemoryAuthors.put("Donald Knuth", 1);
        inMemoryAuthors.put("Cormen", 1);
    }

    /**
     * Adds a new author to the database or in-memory store.
     *
     * @param name The author name
     * @param biography The author biography (optional)
     * @return true if successfully added, false otherwise
     */
    public boolean addAuthor(String name, String biography) {
        if (isDatabaseAvailable) {
            String sql = "INSERT INTO authors (name, biography) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                ps.setString(2, biography != null ? biography : "");
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Error adding author: " + e.getMessage());
                return false;
            }
        } else {
            // In-memory mode
            if (inMemoryAuthors.containsKey(name.trim())) {
                return false; // Already exists
            }
            inMemoryAuthors.put(name.trim(), 0);
            return true;
        }
    }

    /**
     * Retrieves all authors with their book counts.
     *
     * @return A LinkedHashMap of author names to book counts
     */
    public LinkedHashMap<String, Integer> getAllAuthorsWithCounts() {
        if (isDatabaseAvailable) {
            LinkedHashMap<String, Integer> authors = new LinkedHashMap<>();
            String sql = "SELECT a.id, a.name, COUNT(ba.book_id) as book_count " +
                         "FROM authors a " +
                         "LEFT JOIN book_authors ba ON a.id = ba.author_id " +
                         "GROUP BY a.id, a.name " +
                         "ORDER BY a.name";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String authorName = rs.getString("name");
                    int bookCount = rs.getInt("book_count");
                    authors.put(authorName, bookCount);
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving authors: " + e.getMessage());
            }
            return authors;
        } else {
            // Return in-memory authors
            return new LinkedHashMap<>(inMemoryAuthors);
        }
    }

    /**
     * Gets an author by name.
     *
     * @param name The author name
     * @return The author ID, or -1 if not found
     */
    public int getAuthorByName(String name) {
        if (isDatabaseAvailable) {
            String sql = "SELECT id FROM authors WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error getting author: " + e.getMessage());
            }
            return -1;
        } else {
            // In-memory mode
            return inMemoryAuthors.containsKey(name.trim()) ? 1 : -1;
        }
    }

    /**
     * Deletes an author by name.
     *
     * @param name The author name
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteAuthor(String name) {
        if (isDatabaseAvailable) {
            String sql = "DELETE FROM authors WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name.trim());
                int affectedRows = ps.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Error deleting author: " + e.getMessage());
                return false;
            }
        } else {
            // In-memory mode
            return inMemoryAuthors.remove(name.trim()) != null;
        }
    }

    /**
     * Gets the count of books by an author.
     *
     * @param authorName The author name
     * @return The book count
     */
    public int getBookCountForAuthor(String authorName) {
        if (isDatabaseAvailable) {
            String sql = "SELECT COUNT(*) as count FROM book_authors WHERE author_id = (SELECT id FROM authors WHERE name = ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, authorName.trim());
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
            return inMemoryAuthors.getOrDefault(authorName.trim(), 0);
        }
    }
}
