package service;

import model.Book;
import repository.BookRepository;
import util.DataManager;
import java.util.List;

/**
 * BookService.java
 * Provides business logic for book catalogue management.
 * Acts as an intermediary between controllers and the data access layer.
 */
public class BookService {

    private BookRepository bookRepository;

    public BookService() {
        this.bookRepository = DataManager.getInstance().getBookRepository();
    }

    /**
     * Adds a new book to the catalogue.
     *
     * @param book The book to add.
     * @return true if successfully added.
     */
    public boolean addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Retrieves a book by ID.
     *
     * @param bookId The book identifier.
     * @return The Book object or null if not found.
     */
    public Book getBook(String bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Updates book details.
     *
     * @param book The book with updated information.
     * @return true if successfully updated.
     */
    public boolean updateBook(Book book) {
        return bookRepository.update(book);
    }

    /**
     * Deletes (soft-deletes) a book from the catalogue.
     *
     * @param bookId The book to delete.
     * @return true if successfully deleted.
     */
    public boolean deleteBook(String bookId) {
        Book book = bookRepository.findById(bookId);
        if (book != null) {
            book.deactivate();
            return bookRepository.update(book);
        }
        return false;
    }

    /**
     * Retrieves all active books in the catalogue.
     *
     * @return List of active books.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findActive();
    }

    /**
     * Searches for books by title, author, or ISBN.
     *
     * @param query The search query.
     * @return List of matching books.
     */
    public List<Book> searchBooks(String query) {
        return bookRepository.search(query);
    }
}
