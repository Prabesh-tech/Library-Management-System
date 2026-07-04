package repository;

import model.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookRepository.java
 * Data access layer for Book entities.
 * Currently uses in-memory storage; can be replaced with file/DB backend.
 */
public class BookRepository {

    private Map<String, Book> bookStore;

    public BookRepository() {
        this.bookStore = new HashMap<>();
    }

    /**
     * Saves a new book to the repository.
     *
     * @param book The book to save.
     * @return true if successfully saved.
     */
    public boolean save(Book book) {
        if (book != null && book.getBookId() != null) {
            bookStore.put(book.getBookId(), book);
            return true;
        }
        return false;
    }

    /**
     * Finds a book by its ID.
     *
     * @param bookId The book identifier.
     * @return The Book or null if not found.
     */
    public Book findById(String bookId) {
        return bookStore.get(bookId);
    }

    /**
     * Updates an existing book.
     *
     * @param book The book with updated information.
     * @return true if successfully updated.
     */
    public boolean update(Book book) {
        if (book != null && book.getBookId() != null && bookStore.containsKey(book.getBookId())) {
            bookStore.put(book.getBookId(), book);
            return true;
        }
        return false;
    }

    /**
     * Deletes a book from the repository.
     *
     * @param bookId The book to delete.
     * @return true if successfully deleted.
     */
    public boolean delete(String bookId) {
        return bookStore.remove(bookId) != null;
    }

    /**
     * Finds all books in the repository.
     *
     * @return List of all books (active and inactive).
     */
    public List<Book> findAll() {
        return new ArrayList<>(bookStore.values());
    }

    /**
     * Finds all active books in the catalogue.
     *
     * @return List of active books only.
     */
    public List<Book> findActive() {
        List<Book> activeBooks = new ArrayList<>();
        for (Book book : bookStore.values()) {
            if (book.isActive()) {
                activeBooks.add(book);
            }
        }
        return activeBooks;
    }

    /**
     * Searches for books by title, author, or ISBN (case-insensitive).
     *
     * @param query The search query.
     * @return List of matching books.
     */
    public List<Book> search(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Book book : bookStore.values()) {
            if (book.isActive() 
                && (book.getTitle().toLowerCase().contains(lowerQuery)
                    || book.getAuthor().toLowerCase().contains(lowerQuery)
                    || book.getIsbn().contains(query))) {
                results.add(book);
            }
        }
        return results;
    }
}
