package service;

import repository.CategoryRepository;
import java.util.LinkedHashMap;

/**
 * CategoryService.java
 * Provides business logic for category management.
 * Acts as an intermediary between controllers/views and the data access layer.
 */
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    /**
     * Adds a new category.
     *
     * @param name The category name
     * @param description The category description (optional)
     * @return true if successfully added, false otherwise
     */
    public boolean addCategory(String name, String description) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return categoryRepository.addCategory(name.trim(), description);
    }

    /**
     * Retrieves all categories with their book counts.
     *
     * @return A LinkedHashMap of category names to book counts
     */
    public LinkedHashMap<String, Integer> getAllCategoriesWithCounts() {
        return categoryRepository.getAllCategoriesWithCounts();
    }

    /**
     * Gets a category ID by name.
     *
     * @param name The category name
     * @return The category ID, or -1 if not found
     */
    public int getCategoryByName(String name) {
        return categoryRepository.getCategoryByName(name);
    }

    /**
     * Deletes a category by name.
     *
     * @param name The category name
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return categoryRepository.deleteCategory(name.trim());
    }

    /**
     * Gets the book count for a category.
     *
     * @param categoryName The category name
     * @return The book count
     */
    public int getBookCountForCategory(String categoryName) {
        return categoryRepository.getBookCountForCategory(categoryName);
    }

    /**
     * Checks if a category exists.
     *
     * @param name The category name
     * @return true if the category exists, false otherwise
     */
    public boolean categoryExists(String name) {
        return getCategoryByName(name) != -1;
    }
}
