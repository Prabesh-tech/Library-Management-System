package controller;

import model.Book;
import service.BookService;
import exception.BookNotFoundException;
import exception.InvalidInputException;
import java.util.List;

/**
 * BookController.java
 * Handles all book-related operations and mediates between the view
 * and the BookService business logic layer.
 */
public class BookController {

    private BookService bookService;

    public BookController() {
        this.bookService = new BookService();
    }

    // ─── Book CRUD Operations ─────────────────────────────────────────────

    /**
     * Adds a new book to the library catalogue.
     *
     * @param book The book to add.
     * @return true if successfully added.
     * @throws InvalidInputException if book data is invalid.
     */
    public boolean addBook(Book book) throws InvalidInputException {
        if (book == null || book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("title", "Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new InvalidInputException("author", "Author name cannot be empty");
        }
        return bookService.addBook(book);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId The unique book identifier.
     * @return The book object if found.
     * @throws BookNotFoundException if book does not exist.
     */
    public Book getBook(String bookId) throws BookNotFoundException {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        return book;
    }

    /**
     * Updates an existing book's details.
     *
     * @param book The book with updated information.
     * @return true if successfully updated.
     * @throws BookNotFoundException if book does not exist.
     */
    public boolean updateBook(Book book) throws BookNotFoundException {
        if (bookService.getBook(book.getBookId()) == null) {
            throw new BookNotFoundException(book.getBookId());
        }
        return bookService.updateBook(book);
    }

    /**
     * Soft-deletes a book from the catalogue.
     *
     * @param bookId The book to delete.
     * @return true if successfully deleted.
     * @throws BookNotFoundException if book does not exist.
     */
    public boolean deleteBook(String bookId) throws BookNotFoundException {
        if (bookService.getBook(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        return bookService.deleteBook(bookId);
    }

    /**
     * Retrieves all active books in the catalogue.
     *
     * @return List of books currently available.
     */
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Searches for books by title, author, or ISBN.
     *
     * @param query Search query string.
     * @return List of matching books.
     */
    public List<Book> searchBooks(String query) {
        return bookService.searchBooks(query);
    }

    /**
     * Checks if a book is available for borrowing.
     *
     * @param bookId The book to check.
     * @return true if at least one copy is available.
     */
    public boolean isBookAvailable(String bookId) {
        Book book = bookService.getBook(bookId);
        return book != null && book.isAvailable();
    }
}
