package exception;

/**
 * UserNotFoundException.java
 * Thrown when a requested user account cannot be found in the repository.
 */
public class UserNotFoundException extends RuntimeException {
    private final String userId;

    public UserNotFoundException(String userId) {
        super("User not found with ID or email: " + userId);
        this.userId = userId;
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.userId = null;
    }

    public String getUserId() { return userId; }
}
