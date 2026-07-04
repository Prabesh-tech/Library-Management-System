package service;

import repository.AuthorRepository;
import java.util.LinkedHashMap;

/**
 * AuthorService.java
 * Provides business logic for author management.
 * Acts as an intermediary between controllers/views and the data access layer.
 */
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService() {
        this.authorRepository = new AuthorRepository();
    }

    /**
     * Adds a new author.
     *
     * @param name The author name
     * @param biography The author biography (optional)
     * @return true if successfully added, false otherwise
     */
    public boolean addAuthor(String name, String biography) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return authorRepository.addAuthor(name.trim(), biography);
    }

    /**
     * Retrieves all authors with their book counts.
     *
     * @return A LinkedHashMap of author names to book counts
     */
    public LinkedHashMap<String, Integer> getAllAuthorsWithCounts() {
        return authorRepository.getAllAuthorsWithCounts();
    }

    /**
     * Gets an author ID by name.
     *
     * @param name The author name
     * @return The author ID, or -1 if not found
     */
    public int getAuthorByName(String name) {
        return authorRepository.getAuthorByName(name);
    }

    /**
     * Deletes an author by name.
     *
     * @param name The author name
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteAuthor(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return authorRepository.deleteAuthor(name.trim());
    }

    /**
     * Gets the book count for an author.
     *
     * @param authorName The author name
     * @return The book count
     */
    public int getBookCountForAuthor(String authorName) {
        return authorRepository.getBookCountForAuthor(authorName);
    }

    /**
     * Checks if an author exists.
     *
     * @param name The author name
     * @return true if the author exists, false otherwise
     */
    public boolean authorExists(String name) {
        return getAuthorByName(name) != -1;
    }
}
