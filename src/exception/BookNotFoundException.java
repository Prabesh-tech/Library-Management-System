package exception;

/*
 * BookNotFoundException.java
 * Thrown when a requested book cannot be found in the repository.
 */
public class BookNotFoundException extends RuntimeException {
    private final String bookId;

    public BookNotFoundException(String bookId) {
        super("Book not found with ID: " + bookId);
        this.bookId = bookId;
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.bookId = null;
    }

    public String getBookId() { return bookId; }
}
